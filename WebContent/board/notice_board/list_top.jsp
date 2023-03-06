		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../board/images/blet.gif" align="absmiddle"> 공지게시판</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'>
<!--
			<%
				if((String)session.getAttribute(tablename + "_adminid") == null){
			%>
				  <a href='<%=link_login%>' onMouseOver="window.status='게시판 관리자 로그인';return true" onMouseOut="window.status=''"><img src="../board/images/_admin.gif" border="0" alt="관리자 로그인"></a>
			<%
				}
			%>
-->			
			  <img src="../board/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../board/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../board/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR>
			<TR><TD height='1' bgcolor='#9DA8BA' colspan="3"></TD></TR></TBODY>
		</TABLE>