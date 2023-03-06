<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String c_class		= request.getParameter("c_class")==null?"delivery_period":request.getParameter("c_class");
	String mode			= request.getParameter("mode");
	String query		= "";
	bean.openConnection();
%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center"><!--타이틀-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../em/images/pop_input_item_mgr.gif" border='0' align='absmiddle' alt='입력항목관리'></TD></TR>
				<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
	<TR><TD height=35><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0 border=0>
			<TBODY>
				<TR><TD align=left style='padding-left:12px'>
					<form name="category" method="post" action="input_item_mgr.jsp" style="margin:0">
					<select name="c_class" onChange="javascript:document.category.submit();" <% if(mode == null) out.print("disabled");%>>
						<%
							query = "SELECT DISTINCT(item_class),class_name FROM em_input_item_table";
							bean.executeQuery(query);
							while(bean.next()){
						%>
							<option value="<%=bean.getData("item_class")%>" <% if(c_class.equals(bean.getData("item_class"))) out.print("selected"); %>><%=bean.getData("class_name")%></option>
						<%	}	%>
					</select> <a href="javascript:add_item('<%=c_class%>');"><img src='../images/bt_add.gif' border='0' align='absmiddle'></a> <%if(mode == null){%><a href="javascript:self.close();"><img src='../images/bt_close.gif' border='0' align='absmiddle'></a><%}%><input type='hidden' name='mode' value='<%=mode%>'></form>
				</TD></TR></TBODY></TABLE></TD></TR>
				
	<TR><TD height='394'>
	<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
		<TABLE  cellSpacing=0 cellPadding=0 width="98%" border=0 align='center'>
			<tbody>
				<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
         		<TR height=23>  
					<TD noWrap width=220 align=middle class='list_title'>항목명</TD>
					<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
					<TD noWrap width=100 align=middle class='list_title'>비고</TD>
					<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
					<TD noWrap width=100% align=middle class='list_title'></TD>
				</TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<%
			query = "SELECT mid,item_name,item_code FROM em_input_item_table WHERE item_class = '" + c_class + "'";
			bean.executeQuery(query);
			while(bean.next()){
%>

				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					<TD align=middle height="24" class='list_bg'><%=bean.getData("item_name")%></td>
					<TD><IMG height=1 width=1></TD>
					<TD align=middle class='list_bg'><a href="javascript:sel_this('<%=bean.getData("item_name")%>');"><img src='../images/lt_sel.gif' border='0' align='absmiddle'></a> <a href="javascript:modify_item('<%=bean.getData("mid")%>');"><img src='../images/lt_modify.gif' border='0' align='absmiddle'></a></td>
					<TD><IMG height=1 width=1></TD>
					<TD align=middle class='list_bg'></td>
				</TR>
				<TR><TD colSpan=9 background="../cm/images/dot_line.gif"></TD></TR>
<%			}
%>			
			</TBODY></TABLE></DIV></TD></TR>

			<!--꼬릿말-->
			<TR><TD>
				<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
				<TBODY>
					<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
						<a href='javascript:self.close()'><img src='../cm/images/bt_close.gif' border='0' align='absmiddle'></a></TD>
					</TR>
					<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR>
	        </TBODY></TABLE></TD></TR>


</TD></TR>
</TABLE>
</BODY>
</HTML>

<script>

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function add_item(item_class) {
	var url = "add_item.jsp?mode=add&item_class="+item_class;
//	window.open(url,'add_item','width=400,height=100,scrollbars=yes,toolbar=no,status=no,resizable=no');
	wopen(url,'add_item','250','142','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function modify_item(mid) {
	var url = "add_item.jsp?mode=modify&mid="+mid;
//	window.open(url,'modify_item','width=400,height=100,scrollbars=yes,toolbar=no,status=no,resizable=no');
	wopen(url,'add_item','250','142','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function sel_this(item_name){
	window.returnValue = item_name;
	self.close();
}
</script>