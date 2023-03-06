<form method=post name="writeForm" action='AnBBoard?tablename=<%=tablename%>&upload_size=<%=upload_size%>' enctype='multipart/form-data' style='margin:0'><%=input_hidden%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--등록정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">작성자</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=division%>/<%=name%>/<%=id%><input type=hidden name=writer size=15 maxlength=10 value="<%=division%>/<%=name%>/<%=id%>"><input type=hidden name=password size=10 maxlength=10 value="<%=passwd%>"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">제목</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type=text name=subject size=82 maxlength=<%=l_maxsubjectlen%> value="<%=subject%>" class='text_01'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">선택사항</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
				<%
					if(enablecategory > 0){
						if("reply".equals(mode)){
							int j=1;
							while(category_iter.hasNext()){
								String category_name = (String)category_iter.next();
								if(Integer.toString(j).equals(category)){
				%>
							<input type=hidden name=category value=<%=j%>><font class=kissofgod-comment-textarea>[ <%=category_name%> ]</font>카테고리
				<%
								}// if end
							j++;
							}// while end
						}// if end
						else{
				%>
				            <select name=category>
				            <option class=kissofgod-comment-textarea>카테고리를 선택하세요.</option>
				<%
							int j=1;
							while(category_iter.hasNext()){
								String selected = "";
								if(Integer.toString(j).equals(category)) selected = "selected";
				%>
				            <option value=<%=j%> class=kissofgod-comment-textarea <%=selected%>><%=(String)category_iter.next()%></option>
				<%
							j++;
							}// while end
				%>
				            </select>
				<%
						}// else end
					}// if(enablecategory > 0) end
				%>
				            <input type=checkbox name=html value='y' style='border:0' <%=chk_html%>> HTML사용 
				<%
					if(((String)session.getAttribute(tablename + "_adminid") != null) && ("write".equals(mode))){
				%>
							<input type=checkbox name=notice value='y' style='border:0'> 공지사항
				<%
					}
				%>
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">내용</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><TEXTAREA name=content ROWS=19 COLS=100 WRAP=VIRTUAL class='text_01'><%=content%></TEXTAREA></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
		   		<%
					if (enableupload > 0){
				%>
		            <%=file_stat%>
				<%
						if(i < enableupload){
				%>
				            <input type=file name=attachfile<%=i%> onClick='fileadd_action<%=i%>()' size=45>
				            <font id=id<%=i%>></font>
				<%
						}else if(i == enableupload){
				%>
				            <input type=file name=attachfile<%=i%> size=45>
				            <font id=id<%=i%>></font>
				<%
						}
					}
				%><br>제한용량 : <%=upload_size%> Mbytes</td></tr>
		 <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr></tbody></table></form>
</td></tr></table>