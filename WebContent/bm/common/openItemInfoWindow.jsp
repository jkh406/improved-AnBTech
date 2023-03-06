<%@ page 
	info = ""
	language = "java"
	contentType = "text/html;charset=KSC5601"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.pu.entity.*,com.anbtech.st.entity.*"
%>

<%	// 호출(요구)정보 field명 
	String item_code = request.getParameter("item_code");
	String item_name = request.getParameter("item_name");
	String item_desc = request.getParameter("item_desc");
	String item_unit = request.getParameter("item_unit");
	String item_type = request.getParameter("item_type");
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../cm/css/style.css">
<HEAD>
<TITLE>품목검색</TITLE>
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM name="eForm">

<TABLE  cellSpacing=0 cellPadding=0 width="100%">
	<TR><TD>
		<TBODY>
		<TR><TD height="3" bgcolor="0C2C55" colspan='2'></TD></TR>
		<TR>
			<TD height="33" valign="middle" bgcolor="#73AEEF" ><img src="" hspace="10" alt='품목검색'></TD>
			<TD style="padding-right:10px" align='right' valign='middle'  bgcolor="#73AEEF"></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA' colspan='2'></TD></TR>
		<TR><TD height="2" bgcolor="2167B6"  colspan='2'></TD></TR>
		</TBODY></TABLE></TD></TR>
	
	<TR><TD height=22 colspan="2">&nbsp;</TD></TR>
		<TBODY>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR><TD width="36%" style="padding-left:4px" background="images/bg-011.gif">
				<IFRAME name="search" src="../servlet/CodeMgrServlet?mode=list_item_p" width="100%" height="350" border="0" frameborder="0" scrolling="yes">브라우저가 인라인 프레임을 지원하지 않거나 현재 인라인 프레임을 표시하지 않도록 설정되어 있습니다.</IFRAME></TD></TR>
        <TR bgcolor="C7C7C7"><TD height="1" colspan="2"></TD></TR>
        </TBODY></TD></TR>
</TABLE>
<!--꼬릿말-->
<TR><TD height='1' bgcolor='#9DA8BA' colspan='2'></TD></TR>
<TABLE>
	<TBODY><TR><TD width='820' height="33" align="right" bgcolor="#73AEEF" style='padding-right:4px'>
		<A href='javascript:self.close()'><img src='../cm/images/bt_close.gif' border='0' align='absright'></a></TD></TR>
	<TR><TD width="820" height=3 bgcolor="0C2C55" colspan='3'></TD></TR>
</TBODY>
</TABLE>
	<!-- IFRAME에서 받아오는 변수 값 -->
	<INPUT type='hidden' name='item_code'>
	<INPUT type='hidden' name='item_name'>
	<INPUT type='hidden' name='item_desc'>
	<INPUT type='hidden' name='item_unit'>
	<INPUT type='hidden' name='item_type'>

	<!-- 호출 페이지의 Field명 -->
	<INPUT type='hidden' name='field_item_code' value='<%=item_code%>'>
	<INPUT type='hidden' name='field_item_name' value='<%=item_name%>'>
	<INPUT type='hidden' name='field_item_desc' value='<%=item_desc%>'>
	<INPUT type='hidden' name='field_item_unit' value='<%=item_unit%>'>
	<INPUT type='hidden' name='field_item_type' value='<%=item_type%>'>

</FORM>
</BODY>
</HTML>

<script language='javascript'>
	
	function return_value(){
		var f = document.forms[0];

		var item_code = f.item_code.value;
		var item_name = f.item_name.value;
		var item_desc = f.item_desc.value;
		var item_unit = f.item_unit.value;
		var item_type = f.item_type.value;

		var f_item_code = f.field_item_code.value;
		var f_item_name = f.field_item_name.value;
		var f_item_desc = f.field_item_desc.value;
		var f_item_unit = f.field_item_unit.value;
		var f_item_type = f.field_item_type.value;

		if(opener.document.forms[0].<%=item_code%>){
			opener.document.forms[0].<%=item_code%>.value = item_code;
		}

		if(opener.document.forms[0].<%=item_name%>){
			opener.document.forms[0].<%=item_name%>.value = item_name;
		}

		if(opener.document.forms[0].<%=item_desc%>){
			opener.document.forms[0].<%=item_desc%>.value = item_desc;
		}

		if(opener.document.forms[0].<%=item_unit%>){
			opener.document.forms[0].<%=item_unit%>.value = item_unit;
		}

		if(opener.document.forms[0].<%=item_type%>){
			opener.document.forms[0].<%=item_type%>.value = item_type;
		}

		self.close();
	}

</script>