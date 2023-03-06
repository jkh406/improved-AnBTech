<%
String bbs  = request.getParameter("bbs");
if(bbs == null) bbs = "notice_board";
%>
<HTML><HEAD><TITLE>게시판</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<META content="MSHTML 6.00.2800.1170" name=GENERATOR></HEAD><FRAMESET border=0 
frameSpacing=0 frameBorder=0 cols=175,*><FRAMESET border=0 frameSpacing=0 
rows=82,*><FRAME name=title-frame marginWidth=0 marginHeight=0 
src="BbsTitle.htm" frameBorder=NO scrolling=no><FRAME name=Left 
marginWidth=0 marginHeight=0 src="../servlet/AnBBoard?tablename=<%=bbs%>&mode=menu" frameBorder=0 
noResize></FRAMESET><FRAME name=up marginWidth=0 marginHeight=0 
src="../servlet/AnBBoard?tablename=<%=bbs%>" frameBorder=0 noResize 
scrolling=yes></FRAMESET></HTML>
