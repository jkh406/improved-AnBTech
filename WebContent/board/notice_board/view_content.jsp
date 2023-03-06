<%
	int i = 0;
	//루프로 가져 온것을 다 내보낸다..ㅡㅡ;;
	while(table_iter.hasNext()){
		table = (Table)table_iter.next();
		String w_time = table.getW_time();
		String u_time = table.getU_time();
		String pos = table.getPos();
		String writer = table.getWriter();
		String writer_s = com.anbtech.text.StringProcess.repWord(writer, "\"", "&quot;");
		writer_s = com.anbtech.text.StringProcess.repWord(writer_s, "'", "`");//writer_s 는 "",''처리를 위해 만듬(' ->`)
		String homepage = table.getHomepage();
		String email = table.getEmail();
		String subject = table.getSubject();
		String content = com.anbtech.text.StringProcess.getContentTxt(table.getContent());
		String no = table.getNo();
		String category = table.getCategory();
		String ip_addr = table.getIp_addr();
		String filelink = table.getFilelink();
		String filepreview = table.getFilepreview();

		redirect = (Redirect)redirect_iter.next();
		String link_mailto = redirect.getLink_mailto();
		String link_homepage = redirect.getLink_homepage();
		String link_modify = redirect.getLink_modify();
		String link_delete = redirect.getLink_delete();
		String link_download = redirect.getLink_download();
		String link_vote = redirect.getLink_vote();
		String link_reply = redirect.getLink_reply();
		String link_list = redirect.getLink_list();
		String input_hidden = redirect.getInput_hidden();
%>
<!-- 메뉴 시작 -->
<TABLE height=33 cellSpacing=0 cellPadding=0 width="100%" border='0'>
	<TBODY>
	<TR>
	  <TD width=4>&nbsp;</TD>
	  <TD align=left width='400'>
					  <a href='<%=link_list%>' onMouseOver="window.status='List';return true" onMouseOut="window.status=''"><img src='../board/images/bt_list.gif' border='0' align='absmiddle'></a> 	  
			<%	if (writer.equals(division+"/"+name+"/"+id)){	%>
						<a href='<%=link_modify%>' onMouseOver="window.status='Modify Item';return true" onMouseOut="window.status=''"><img src='../board/images/bt_modify.gif' border='0' align='absmiddle'></a> 
			<%		}	%>
			
			<%	if (writer.equals(division+"/"+name+"/"+id) || is_owner){	%>
						<a href='<%=link_delete%>' onClick="if(confirm('정말 삭제하시겠습니까?')){return true;}else{return false;}" onMouseOver="window.status='Delete Item';return true" onMouseOut="window.status=''"><img src='../board/images/bt_del.gif' border='0' align='absmiddle'></a>
			<%	}	%>

			<%	if ("n".equals(adminonly) || is_owner){	%>
					  <a href='<%=link_reply%>' onMouseOver="window.status='Reply to article';return true" onMouseOut="window.status=''"><img src='../board/images/bt_reply.gif' border='0' align='absmiddle'></a>
			<%	}	%></TD></TR></TBODY>
</TABLE>
<!-- 메뉴 끝 -->

<!-- 본문 시작 -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--등록정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">제목</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=pos%>. <b><%=subject%></b></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">등록자</td>
           <td width="37%" height="25" class="bg_04"><%=writer%></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">작성일자</td>
           <td width="37%" height="25" class="bg_04"><%=w_time%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
			<%	if(enableupload > 0 && filelink.length() > 10 ){	%>
					<%=filelink%><br>
			<%	}	%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table>

	<!--본문내용-->
	 <table cellpadding="0" cellspacing="10" width="100%" border="0">
		<tr><td width="100%" align="left"><%=content%></td></tr>
		<tr><td width="100%">
			<%
					if("y".equals(enablecomment) && ((ArrayList)request.getAttribute("Table_Cmt_List_"+i)).size() > 0){
			%>
					<table width="98%" cellpadding="0" cellspacing="0" align='center' border="0">
			<%
						//table_cmt_list에서 가져오기
						ArrayList table_cmt_list = new ArrayList();
						table_cmt_list = (ArrayList)request.getAttribute("Table_Cmt_List_"+i);
						table_cmt = new Table_Cmt();
						Iterator table_cmt_iter = table_cmt_list.iterator();
						while(table_cmt_iter.hasNext()){
						table_cmt = (Table_Cmt)table_cmt_iter.next();
						String writer_cmt = table_cmt.getWriter();
						String comment_cmt = table_cmt.getComment();
						String w_time_cmt = table_cmt.getW_time();
						String input_hidden_cmt = table_cmt.getPasswd();
			%>
					  <form method=post action='AnBBoard?tablename=<%=tablename%>' enctype='multipart/form-data' onSubmit="if(!this.password.value){alert('비밀번호를 입력하세요.');this.password.focus();return false;} else return true;" name=cmForm>
					  <%=input_hidden_cmt%>
						<tr>
						  <td width="70" valign="top"> <b><%=writer_cmt%></b></td>
						  <td width="180" valign="top"> (<%=w_time_cmt%>)</td>
						  <td valign="top"><%=comment_cmt%></td>
						  <td align="right" valign="top">
						  <% if(writer_cmt.equals(name)){%>
								<input type="hidden" name="password" size="20" maxlength="20" value="<%=passwd%>"><input type="submit" value="delete">
						  <% }else{ %>
								&nbsp;
						  <% } %>
						  </td>
						</tr></form>
			<%			} // end while	%>
					</table>
			<%		} //end if	%>

			<%		if ("y".equals(enablecomment)){	%>
				  <br>
					<form method=post action='AnBBoard?tablename=<%=tablename%>' enctype='multipart/form-data' onSubmit="if(!this.writer.value || this.writer.value == '성명'){alert('성명을 입력하세요.');this.writer.focus();return false;}else if(!this.comment.value || this.comment.value == '간단한 의견(30자 이내)'){alert('의견을 입력하세요.');this.comment.focus();return false;}else if(!this.password.value){alert('비밀번호를 입력하세요.');this.password.focus();return false;} else return true;" name=cmForm>
					<%=input_hidden%>
					  <table cellpadding=0 cellspacing=0 border=0 width='98%' align=center>
						<tr>
						  <td align=right><textarea name="comment" cols="70" rows="3" onFocus="this.value=''">이 글에 대한 의견이 있으면 간단히 적어 주세요.(100글자 아내)</textarea></td>
						</tr>
						<tr>
						  <td align=right><INPUT type=submit value='save'></td>
						</tr>
					  </table>
					  <input type=hidden name=writer value="<%=name%>">
					  <input type=hidden name=password value='<%=passwd%>'>
					</form>
			<%		}	%>

			<%
					i++;
				} // end while
			%>
		</td></tr></table>

</td></tr></table>