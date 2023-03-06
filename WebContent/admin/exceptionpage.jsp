<%@ page language="java" contentType="text/html;charset=euc-kr" %>
<%@ page import="java.io.*,com.anbtech.text.Hanguel" %>
<%@ include file="config.jsp"%>
<%
	String msg = (String)request.getAttribute("ERR_MSG");
%>

<HTML>
<HEAD>
<TITLE>::::: 에러페이지 :::::</TITLE>
<LINK href="<%=server_path%>/admin/css/style.css" type=text/css rel=stylesheet>
</HEAD>

<BODY text=#000000 bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
      <TABLE cellSpacing=0 cellPadding=0 width=611 border=0 align=center>
        <TBODY>
        <TR>
          <TD height=2><IMG height=34 src="<%=server_path%>/admin/images/space_com_main_03.gif" 
            width=1></TD></TR>
        <TR>
          <TD height=2><IMG height=42 src="<%=server_path%>/admin/images/tit_com_error.gif" 
            width=611></TD></TR>
        <TR>
          <TD height=2>&nbsp;</TD></TR>
        <TR>
          <TD align=right height=13>&nbsp;</TD></TR>
        <TR>
          <TD height=2>
            <TABLE cellSpacing=1 cellPadding=15 width=612 bgColor=#b9d4e9 
            border=0>
              <TBODY>
              <TR align=middle bgColor=#eef3f7>
                <TD bgColor=#eef3f7 height=27>
                  <TABLE cellSpacing=5 cellPadding=0 width=526 border=0>
                    <TBODY>
                    <TR>
                      <TD align=middle width=167 height=100 rowSpan=5><IMG 
                        height=131 src="<%=server_path%>/admin/images/pic_com_confirm.gif" 
                        width=141></TD>
                      <TD width=344></TD></TR>
                    <TR>
                      <TD width=400 height=16>죄송합니다. 예상치 못한 에러가 발생했습니다.<br>다시 시도해 보시고, 동일한 문제가 계속 발생시 시스템 관리자에게 문의해 주십시오.<br><br>대리 박동렬 ☎ 031)632-5330,5331 <A 
                        href="mailto:subaksi@anbtech.co.kr?cc=yukjm64@anbtech.co.kr&bcc=jsc21@anbtech.co.kr&subject=Error Report 메일&body=<%=msg%>">subaksi@anbtech.co.kr</A></TD></TR>
                    <TR>
                      <TD width=400 height=16>&nbsp;</TD></TR>
                    <TR>
                      <TD width=400 height=16>에러메시지:<br><textarea rows="5" cols="55"><%=msg%></textarea></TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE></TD></TR>
        <TR>
          <TD>&nbsp;</TD></TR>
        <TR>
          <TD>&nbsp;</TD></TR>
        <TR>
          <TD align=middle><A href="javascript:history.go(-1);"><IMG height=21 src="<%=server_path%>/admin/images/btn_com_ok.gif" width=82 border=0></A> 
        </TD></TR>
        <TR>
          <TD height=2><IMG height=34 src="<%=server_path%>/admin/images/space_com_main_03.gif" width=1></TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE>
</BODY></HTML>

