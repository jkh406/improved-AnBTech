<%@ include file="../configPopUp.jsp"%>
<%@ page		
	info= "System �ĺ��ڵ�"		
	contentType = "text/html; charset=euc-kr" 		
%>
<%@	page import="com.anbtech.text.Hanguel" %>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%	
	/*****************************************************************************************
	 * sf		 : ���̸�
	 * div		 : Ư�� '����' �Ǵ� ��� '����' Ȯ�� (all: ��籸��  one: Ư������)
	 * type      : Ư������(�ڵ�)
	 * code		 : �ڵ� field 
	 * code_name : �ڵ�� field 
	 *		
	 *      �� �������� ȣ���� ������ 
	 *		searchSystemMinorCode.jsp?sf=���̸�&div=���д��&type=Ư�������ڵ�&code=�ڵ��ʵ�&code_name=�ڵ���ʵ�
	 *		������ ȣ���Ѵ�.
	 * 
	 * ���ѻ���   :  opener�� Parameter���� ���� ����� �ϳ���(div='one') �ݵ�� type�� Ư�������ڵ��� ���� 
	 *              �־�� �Ѵ�
	 * �߰�����   :  System Minor �ڵ带 type���� �����Ͽ� field���� ��Ÿ���� ���� parameter�߰�
	 *              System Minor �ڵ� type ��ü�� �������̱� ������ field�� Ư���̸��� �ο��ϱ������.
	 *              (���� DB Table�� field�� �߰����� �ʴ��̻� ��ƴ�.) �׷���, default field ����
	 *              type-> "�з��ڵ�", type_name->"���и�", code ->"�з���", code_name->"�з�����" �̶��ϰ�
	 *              ���� ����� type���� opener jsp���� parameter�� field���� �޾� ǥ���ϵ����Ѵ�.
	 *****************************************************************************************/

	com.anbtech.text.Hanguel hanguel = new com.anbtech.text.Hanguel();
	String sf	= request.getParameter("sf");
	String code	= request.getParameter("code")==null?"na":request.getParameter("code");
	String code_name	= request.getParameter("code_name")==null?"na":request.getParameter("code_name");
	String type = request.getParameter("type")==null?"*":request.getParameter("type");
	String div	= request.getParameter("div")==null?"all":request.getParameter("div");
	String sql="";

	String code_field = request.getParameter("code_field")==null?"�з��ڵ�":hanguel.toHanguel(request.getParameter("code_field"));
	String name_field = request.getParameter("name_field")==null?"���и�":hanguel.toHanguel(request.getParameter("name_field"));
	String minor_code = request.getParameter("minor_code")==null?"�з���":hanguel.toHanguel(request.getParameter("minor_code"));
	String minor_field = request.getParameter("minor_field")==null?"�з�����":hanguel.toHanguel(request.getParameter("minor_field"));
	
	
	bean.openConnection();	
	sql = "SELECT DISTINCT type, type_name FROM system_minor_code";
	bean.executeQuery(sql);
	
	// ȣ���� opener�� Parameter�� div�� ���� 'one'��(Ư������) ��쿡 type(Ư�������ڵ�)���� ������
	// opener�� �ҽ��� ���� ������ �˷��ش�!
	if (div.equals("one") && type.equals("")){
	%>
		<script language='javascript'> alert('ERROR! Ư�������� �ڵ� Parameter ���� �����ϴ�. Source�� Ȯ���� �ֽʽÿ�.');history.go(-1);
		self.close();
		</script>

	<%
	}
%>

<HTML><HEAD><TITLE>System �ĺ��ڵ�</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../../css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<FORM name='frml' action="searchSystemMinorCode.jsp" method="post">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55" colspan='2'></TD></TR>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF" ><img src="../../images/pop_system_code.gif" hspace="10" alt='System �ĺ��ڵ�'></TD>
					<TD style="padding-right:10px" align='right' valign='middle'  bgcolor="#73AEEF"></TD></TR>
			    <TR><TD height='1' bgcolor='#9DA8BA' colspan='2'></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--��ư �� ����¡-->
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="98%" border='0' align='center'>
			<TBODY>
				<TR><TD width=4>&nbsp;</TD>
					<TD align=left width='520'>
					<!-- div: Ư��'����' �Ǵ� ��ü'����' Ȯ�� ���� mode  -->
			<% if(div.equals("all")) {%>
					<SELECT name=type onchange='javascript:document.frml.submit()'>
						<OPTION value=''>��ü</option>
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
  
	<TR height=100%><!--����Ʈ-->
		<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="98%" align='center'>
			<TBODY>
		    <TR><TD height='2' bgcolor='#9CA9BA' colspan='13'></TD></TR>
			
			<TR vAlign=middle height=23>
				<TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>
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
				<TD noWrap width=60 align=middle class='list_title'>����</TD>
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

		<!--������-->
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