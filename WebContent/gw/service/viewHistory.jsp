<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	info		= "���� �̷� �󼼺���"		
	contentType = "text/html; charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	String ah_id = request.getParameter("ah_id");	// �̷�ID

	String query = "SELECT a.*,b.name 'uname' FROM history_table a,user_table b WHERE a.ah_id = '"+ah_id+"' and a.au_id = b.id";
	bean.openConnection();	
	bean.executeQuery(query);
	bean.next();

	String s_day		= bean.getData("s_day");
	String s_time		= bean.getData("s_time");
	String v_class		= bean.getData("class");
	String subject		= bean.getData("subject");
	String content		= bean.getData("content");
	String result		= bean.getData("result");
	String file_name	= bean.getData("file_name");
	String file_size	= bean.getData("file_size");
	String umask		= bean.getData("umask");
	String regi_date	= bean.getData("regi_date");
	String companyname = bean.getData("ap_name");
	String customer_name= bean.getData("at_name");
	String writer		= bean.getData("uname");
	String writer_id	= bean.getData("au_id");
	
	s_day = s_day.substring(0,4)+ "�� " + s_day.substring(4,6)+ "�� " + s_day.substring(6,8)+ "��";
	s_time = s_time.substring(0,2)+ "�� " + s_time.substring(3,5)+ "�� ~ " + s_time.substring(6,8)+ "�� " + s_time.substring(9,11) + "��";
%>

<HTML><HEAD><TITLE>���̷� ���� ����</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_service_v.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--��ư
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
       <tbody>
         <tr><td height=20 colspan="4"></td></tr>
		 <tr><td height=25 colspan="4" align="right"><a href="javascript:self.close();"><img src="../images/close.gif" width="46" height="19" border="0"></a></td></tr></tbody></table>-->

    <!--�Է�������-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
       <tbody>
		 <tr bgcolor=#ffffff><td height="10" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4"><img src="../images/service_writer_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�����</td>
           <td width="37%" height="25" class="bg_02"><%=writer%>/<%=writer_id%></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_02"><%=s_day%></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table>

    <!--�̷�����-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=10 colspan="4"></td></tr>
         <tr>
		   <td height=22 colspan="4"><img src="../images/service_history_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�����</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=companyname%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����</td>
           <td width="37%" height="25" class="bg_02"><%=customer_name%></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�ð�</td>
           <td width="37%" height="25" class="bg_02"><%=s_time%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_02">[<%=v_class%>] <%=subject%></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><textarea rows="15" name="content" cols="64" readOnly><%=content%></textarea></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�̽�����</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><textarea rows="5" name="result" cols="64" readOnly><% if(!result.equals("") && result != null && !result.equals("null") ){	%><%=result%> <%}%></textarea></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">÷������</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
		   <% if(!umask.equals("") && umask != null && !umask.equals("null")){	%>
				<a href="downloadp.jsp?fname=<%=file_name%>&fsize=<%=file_size%>&umask=<%=umask%>"><%=file_name%></a>
		   <%} else out.print("÷�ε� ������ �����ϴ�.");%></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../images/bt_close.gif"  hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>