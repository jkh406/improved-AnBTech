		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'><form method=get action='AnBBoard' name=srForm onSubmit="if(this.searchword.value.length< 2){alert('�˻���� 2���̻� �Է��ϼž� �մϴ�.');this.searchword.value='';this.searchword.focus();return false;}else{return true;}" style="margin:0">
				<%=input_hidden_search%>
					  <select name=searchscope>
						  <option value='all' selected>��ü</option>
						  <option value='writer'>����</option>
						  <option value='subject'>����</option>
						  <option value='content'>����</option>
						  <option value='subjectcontent'>����+����</option>	
						  <option value='filename'>���ϸ�</option>
					  </select> <input type=text name=searchword value='' size=15 maxlength=20> <input border=0 onfocus=blur() src="../board/images/bt_search3.gif" border="0" align="absmiddle" type="image">  <a href="<%=link_write%>"><img src="../board/images/bt_add_new.gif" border="0" align="absmiddle"></a>
					<%	if((String)session.getAttribute(tablename + "_adminid") != null){	%>				  
					  <a href="javascript:chkval();"><img src="../board/images/bt_del_sel.gif" border="0" align="absmiddle"></a>
					<%	}	%></form></TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE>