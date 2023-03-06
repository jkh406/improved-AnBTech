<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkGM01.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.gm.entity.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%!
	GoodsInfoItemTable table;
%>

<%
	String mode = request.getParameter("mode");				// 모드

	//선택된 스펙항목에 대한 정보 가져오기
	table = (GoodsInfoItemTable)request.getAttribute("ItemInfo");
	String one_class_list	= table.getOneClass();
	String two_class_list	= table.getTwoClass();
	String item_code		= table.getItemCode();
	String item_name		= table.getItemName();
	String item_value		= table.getItemValue();
	String item_unit		= table.getItemUnit();
	String write_exam		= table.getWriteExam();
	String item_desc		= table.getItemDesc();

	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ItemList");
	table = new GoodsInfoItemTable();
	Iterator table_iter = table_list.iterator();
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../gm/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<FORM ACTION="../servlet/GoodsInfoServlet" METHOD=POST onSubmit="return checkForm()" enctype='multipart/form-data' style="margin:0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../gm/images/blet.gif"> 제품별표준규격설정</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
				<input type="image" onfocus=blur() src="../gm/images/bt_save.gif" border="0" align="absmiddle">
			<%	if(mode.equals("upd_template")){	%>
				<input type="image" onClick="javascript:history.go(-1);" src="../gm/images/bt_cancel.gif" border="0" align="absmiddle">
			<%	}	%>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>
<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">분류선택</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
					<select name="one_class" onChange="javascript:changeClass();" class="text_01">
						<option value="">제품군선택</option><%=one_class_list%>
					</select> 
					<select name="two_class" onChange="javascript:changeClass();" class="text_01">
						<option value="">제품선택</option><%=two_class_list%>
					</select>					
					</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">항목코드</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="item_code" size="8" readOnly value="<%=item_code%>"></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">항목명</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="item_name" size="20" value="<%=item_name%>" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">항목값</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="item_value" size="35" value="<%=item_value%>"></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">단위</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="item_unit" size="35" value="<%=item_unit%>"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">작성예</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="write_exam" size="35" value="<%=write_exam%>"></td>
           <td width="13%" height="25" class="bg_03" background="../qc/images/bg-01.gif">항목설명</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="item_desc" size="35" value="<%=item_desc%>"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
<input type="hidden" name="mode" value="<%=mode%>">		
</form>
<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=80 align=middle class='list_title'>항목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>항목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>항목값</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>작성예</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>항목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>수정</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%
	while(table_iter.hasNext()){
		table = (GoodsInfoItemTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=table.getItemCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getItemName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getItemValue()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getItemUnit()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getWriteExam()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="<%=table.getLinkMod()%>"><img src='../gm/images/lt_modify.gif' border='0'></a></td>
			</TR>
			<TR><TD colSpan=13 background="../dms/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE>

</td></tr></table>
</body>
</html>

<script language="Javascript">

function changeClass(){ 
	var f = document.forms[0];
	var one_class = f.one_class.value;
	var two_class = f.two_class.value;

	location.href="../servlet/GoodsInfoServlet?mode=add_template&one_class=" + one_class + "&two_class=" + two_class;
}

function checkForm()
{
	var f = document.forms[0];

	if(f.item_code.value == 0){
			alert("항목코드가 올바르지 않습니다. 제품분류를 올바르게 선택하십시오.");
			return false;
	}
	if(f.item_name.value == ''){
			alert("항목명을 입력하십시오.");
			f.item_name.focus();
			return false;
	}
}

function viewCode(code)
{
	var f = document.forms[0];
	var one_class = f.one_class.value;
	var two_class = f.two_class.value;

	location.href = "../servlet/GoodsInfoServlet?mode=upd_template&one_class=" + one_class + "&two_class=" + two_class + "&item_code=" + code;
}
</script>