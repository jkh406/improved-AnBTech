<%@ include file="../../../admin/configPopUp.jsp"%>
<%@ page language="java" import="java.sql.*,com.anbtech.text.Hanguel" contentType = "text/html; charset=euc-kr"%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	//���޹��� ���α׷��� (�ٽ� �ش����α׷����� ����� ������)
	String pg = request.getParameter("pg"); if(pg == null) pg = "";		//���α׷���
	String nm = request.getParameter("nm"); if(nm == null) nm = "";		//���޵� ������

	String target = request.getParameter("target");

	//����ó��
	String name = request.getParameter("name") == null?"":Hanguel.toHanguel(request.getParameter("name"));

	bean.openConnection();
	String query = "select pjt_code,pjt_name from prs_project ";
	query += "where pjt_name like '%"+name+"%' ";
	query += "order by pjt_code,in_date";
	bean.executeQuery(query);
%>
<HTML><HEAD><TITLE>������Ʈ�ڵ� ����</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../../images/pop_pjt_sel.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--��ư-->
	<form name="eForm" action="searchProjectCode.jsp" method="post" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
       <tbody>
         <tr>
			<td width="200" height=25 colspan="4" align="left"><input type="text" name="name" size="15"> <a href='javascript:document.eForm.submit();'><img src='../../images/bt_search3.gif' border='0' align='absmiddle'></a><input type="hidden" name="target" size="15" value='<%=target%>'></td>
			<td width="" height=25 colspan="4" align="right"><a href="javascript:self.close();"><img src="../../images/close.gif" width="46" height="19" hspace="10" border="0"></a></td></tr></tbody></table></form>

	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="99%" border=0 valign="top">
	  <TBODY>
	  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	  <TR height=200><!--����Ʈ-->
		<TD vAlign=top>
		  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=23>
				  <TD noWrap width=100 align=middle class='list_title'>������Ʈ�ڵ�</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
				  <TD noWrap width=100% align=middle class='list_title'>������Ʈ��</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
				  <TD noWrap width=60 align=middle class='list_title'>����</TD>
			   </TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
<%
	while(bean.next()){
		String sel = "<a href=\"javascript:opener." + target + ".value='"+bean.getData("pjt_code")+"';self.close();\">����</a>";
%>
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=left height="24" class='list_bg'><%=bean.getData("pjt_code")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='text-indent:10;'><%=bean.getData("pjt_name")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=sel%></td>
				</TR>
				<TR><TD colSpan=5 background="../../images/dot_line.gif"></TD></TR>
	<%
		}
	%>
			</TBODY></TABLE></TD></TR></TBODY></TABLE>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
		   <TR>
            <TD width="100%" height=5 colSpan=4></TD>
          </TR>
		  <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../../images/close.gif" width="46" height="19" hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table>
</BODY></HTML>


<script language="javascript">
<!--

function returnSelected()
{
	var num = document.eForm.pList.selectedIndex;
	var code = document.eForm.pList.options[num].value;
	var name = document.eForm.pList.options[num].text;

	var sel = confirm("������Ʈ�� '"+name+"'�� �����Ͻðڽ��ϱ�?");

	if(sel){
		window.returnValue = code +"|" + name;
		self.close();
	}
}
-->
</script>
