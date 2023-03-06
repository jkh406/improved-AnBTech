<%@ include file="../configPopUp.jsp"%>
<%@ page		
	info= "System 식별코드"		
	contentType = "text/html; charset=euc-kr" 		
%>
<%@	page import="com.anbtech.text.Hanguel" %>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%	
	/*****************************************************************************************
	 * sf		 : 폼이름
	 * div		 : 특정 '구분' 또는 모든 '구분' 확인 (all: 모든구분  one: 특정구분)
	 * type      : 특정구분(코드)
	 * code		 : 코드 field 
	 * code_name : 코드명 field 
	 *		
	 *      이 페이지를 호출할 때에는 
	 *		searchSystemMinorCode.jsp?sf=폼이름&div=구분대상&type=특정구분코드&code=코드필드&code_name=코드명필드
	 *		식으로 호출한다.
	 * 
	 * 제한사항   :  opener의 Parameter에서 구분 대상이 하나면(div='one') 반드시 type은 특정구분코드의 값이 
	 *              있어야 한다
	 * 추가사항   :  System Minor 코드를 type별로 구분하여 field명을 나타내기 위해 parameter추가
	 *              System Minor 코드 type 자체는 가변적이기 때문에 field에 특정이름을 부여하기힘들다.
	 *              (현재 DB Table에 field를 추가하지 않는이상 어렵다.) 그래서, default field 명은
	 *              type-> "분류코드", type_name->"구분명", code ->"분류명", code_name->"분류설명" 이라하고
	 *              현재 적용된 type들은 opener jsp에서 parameter로 field명을 받아 표현하도록한다.
	 *****************************************************************************************/

	com.anbtech.text.Hanguel hanguel = new com.anbtech.text.Hanguel();
	String sf	= request.getParameter("sf");
	String code	= request.getParameter("code")==null?"na":request.getParameter("code");
	String code_name	= request.getParameter("code_name")==null?"na":request.getParameter("code_name");
	String type = request.getParameter("type")==null?"*":request.getParameter("type");
	String div	= request.getParameter("div")==null?"all":request.getParameter("div");
	String sql="";

	String code_field = request.getParameter("code_field")==null?"분류코드":hanguel.toHanguel(request.getParameter("code_field"));
	String name_field = request.getParameter("name_field")==null?"구분명":hanguel.toHanguel(request.getParameter("name_field"));
	String minor_code = request.getParameter("minor_code")==null?"분류명":hanguel.toHanguel(request.getParameter("minor_code"));
	String minor_field = request.getParameter("minor_field")==null?"분류설명":hanguel.toHanguel(request.getParameter("minor_field"));
	
	
	bean.openConnection();	
	sql = "SELECT DISTINCT type, type_name FROM system_minor_code";
	bean.executeQuery(sql);
	
	// 호출한 opener의 Parameter중 div의 값이 'one'일(특정구분) 경우에 type(특정구분코드)값이 없으면
	// opener의 소스에 문제 있음을 알려준다!
	if (div.equals("one") && type.equals("")){
	%>
		<script language='javascript'> alert('ERROR! 특정구분의 코드 Parameter 값이 없습니다. Source를 확인해 주십시요.');history.go(-1);
		self.close();
		</script>

	<%
	}
%>

<HTML><HEAD><TITLE>System 식별코드</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../../css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<FORM name='frml' action="searchSystemMinorCode.jsp" method="post">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55" colspan='2'></TD></TR>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF" ><img src="../../images/pop_system_code.gif" hspace="10" alt='System 식별코드'></TD>
					<TD style="padding-right:10px" align='right' valign='middle'  bgcolor="#73AEEF"></TD></TR>
			    <TR><TD height='1' bgcolor='#9DA8BA' colspan='2'></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--버튼 및 페이징-->
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="98%" border='0' align='center'>
			<TBODY>
				<TR><TD width=4>&nbsp;</TD>
					<TD align=left width='520'>
					<!-- div: 특정'구분' 또는 전체'구분' 확인 가능 mode  -->
			<% if(div.equals("all")) {%>
					<SELECT name=type onchange='javascript:document.frml.submit()'>
						<OPTION value=''>전체</option>
					<% while(bean.next()) {	%>
						<OPTION value='<%=bean.getData("type")%>'><%=bean.getData("type_name")%></option>
					<%	}					%>
					<% 
						if(!type.equals("")) {	%>
						<script>
							document.frml.type.value = '<%=type%>';
						</script>
					<% }
				%> </SELECT>
			<%}%> 
				</TD></TR></TBODY></TABLE></TD></TR>
  
	<TR height=100%><!--리스트-->
		<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="98%" align='center'>
			<TBODY>
		    <TR><TD height='2' bgcolor='#9CA9BA' colspan='13'></TD></TR>
			
			<TR vAlign=middle height=23>
				<TD noWrap width=30 align=middle class='list_title'>번호</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
				<TD noWrap width=150 align=middle class='list_title'><%=code_field%></TD>
				<TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
				<% if(type.equals("") && div.equals("all")) { %>
				<TD noWrap width=100 align=middle class='list_title'><%=name_field%></TD>
				<TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
				<%	}     %>
				<TD noWrap width=100 align=middle class='list_title'><%=minor_code%></TD>
				<TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
				<TD noWrap width=130 align=middle class='list_title'><%=minor_field%></TD>
				<TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
				<TD noWrap width=60 align=middle class='list_title'>편집</TD>
			</TR>			
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%	
		if(type == null || type.equals("")) {
			sql = "SELECT * FROM system_minor_code";
		} else {
			sql = "SELECT * FROM system_minor_code WHERE type = '"+type+"'";
		}
		
		bean.executeQuery(sql);
		int no = 1 ;	

		while(bean.next()){	
		String name = "<a href=\"javascript:returnValue('"+bean.getData("type")+"','"+bean.getData("type_name")+"','"+bean.getData("code")+"','"+bean.getData("code_name")+"');\"><IMG src='../../images/lt_sel.gif' border='0' align='absmiddle'></a>";
%>
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>

			<TD height='24' class='list_bg' align=center><%=no%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD height='24' class='list_bg' align=center><%=bean.getData("type")%></TD>
			<TD><IMG height=1 width=1></TD>
			<% if(type.equals("")&& div.equals("all")) {%>
			<TD height='24' class='list_bg' align=center><%=bean.getData("type_name")%></TD>
			<TD><IMG height=1 width=1></TD>
			<% } %>
			<TD height='24' class='list_bg' align=center><%=bean.getData("code")%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD height='24' class='list_bg' align=center><%=bean.getData("code_name")%></TD>
		    <TD><IMG height=1 width=1></TD>
			<TD height='24' class='list_bg' align=center><%=name%></TD>

		</TR>
		<TR><TD colSpan=13 background="../../images/dot_line.gif"></TD></TR>
<%		no++;
		}
%>
		</TBODY></TABLE></TD></TR>

		<!--꼬릿말-->
        <TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
			<A href='javascript:self.close()'><img src='../../images/bt_close.gif' border='0' align='absmiddle'></A></TD></TR>
        <TR><TD width="100%" height=3 bgcolor="0C2C55"></TD></TR>
     </TBODY></TABLE></TD></TR></TABLE>
<INPUT type='hidden' name=sf value='<%=sf%>'>
<INPUT type='hidden' name=code value='<%=code%>'>
<INPUT type='hidden' name=code_name value='<%=code_name%>'>
<INPUT type='hidden' name=div value='<%=div%>'>


</FORM>
</BODY>
</HTML>


<SCRIPT language="javascript">

function returnValue(type,type_name,code,code_name)
{           
	if(opener.document.<%=sf%>.<%=code%> !=null){
	opener.document.<%=sf%>.<%=code%>.value = code;
	}
	if(opener.document.<%=sf%>.<%=code_name%> !=null){
	opener.document.<%=sf%>.<%=code_name%>.value = code_name;
	}

	self.close();
}

</SCRIPT>