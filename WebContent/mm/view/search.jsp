<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.mm.entity.mfgProductMasterTable table;
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");	//포멧

//	String sItem = request.getParameter("sItem")==null?"":request.getParameter("sItem");
//	String sWord = request.getParameter("sWord")==null?"FAB1":request.getParameter("sItem");
	String msg="";
	String start_date = (String)request.getAttribute("start_date"); if(start_date == null) start_date = "";
	if(start_date.length() != 0) start_date = anbdt.getSepDate(start_date,"/");
	String end_date = (String)request.getAttribute("end_date"); if(end_date == null) end_date = "";
	if(end_date.length() != 0) end_date = anbdt.getSepDate(end_date,"/");
	String sItem = (String)request.getAttribute("sItem"); if(sItem == null) sItem = "";
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String factory_no = (String)request.getAttribute("factory_no"); if(factory_no == null) factory_no = "";
	if(factory_no.length() == 0) msg ="해당공장번호를 선택하십시오.";

%>
<HTML><HEAD><TITLE>상세검색</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<form name="searchForm" style="margin:0">
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td align="center">
	<!--타이틀-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
				<TR>
					<TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_app_op.gif" width="181" height="17" hspace="10" alt='공정검색'></TD></TR>
				<TR>
					<TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
		<table cellspacing=0 cellpadding=0 width="94%" border=0>
			<tbody>
				<tr><td height="12"></td></tr>
				<tr bgcolor="c7c7c7"><td height=1 colspan='2'></td></tr>
				<tr>	
					<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">
					
					<select name="sItem" style=font-size:9pt;color="black";>  
				<%
					String[] sitems = {"","model_name","fg_code","item_code"};
					String[] snames = {"전체검색","모델명","FG코드","재공품코드"};
					String sel = "";
					for(int si=0; si<sitems.length; si++) {
						if(sItem.equals(sitems[si])) sel = "selected";
						else sel = "";
						out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
					}
				%>
					</select>
					</td>
					<td width="87%" height="25" class="bg_04">
						<input type='text' size='20' name='sWord' value='<%=sWord%>'></td>
				</TR>
				<tr bgcolor="c7c7c7"><td height=1 colspan='2'></td></tr>
				<TR>
					<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">공장</td>
					<td width="87%" height="25" class="bg_04">
						<input type='text' size='5' name='factory_no' value='<%=factory_no%>' readonly> 
						<input type='text' size='20' name='factory_name' value='' readonly> 
							<a href="javascript:searchFactoryInfo();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
				<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
				<tr>
					<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">검색일자</td>
					<td width="87%" height="25" class="bg_04">
						<input type='text' size='10' maxlength='10' name='start_date' value='<%=start_date%>' readonly> <a href="Javascript:OpenCalendar('start_date')"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a> ~ <input type='text' size='10' maxlength='10' name='end_date' value='<%=end_date%>' readonly> <a href="Javascript:OpenCalendar('end_date')"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></td>
				<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		</table>		   
	</td></tr>
    
    <tr><td height="13" colspan="2"></td></tr></tbody></table>
	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:go()'><img src='images/bt_search3.gif' border='0' align='absmiddle'></a> <a href='javascript:self.close()'><img src='images/bt_close.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr>
</table>

</form>
</BODY>
</HTML>


<script>

function go() {

	var f = document.searchForm;
	opener.document.forms[0].sItem.value = f.sItem.value;
	opener.document.forms[0].sWord.value = f.sWord.value;
	opener.document.forms[0].factory_no.value = f.factory_no.value;
	opener.document.forms[0].start_date.value = f.start_date.value;
	opener.document.forms[0].end_date.value = f.end_date.value;
	opener.goSearch();
	self.close();
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//공장정보 찾기
function searchFactoryInfo() {
	var f = document.sForm;
	var factory_no = f.factory_no.name;
	var factory_name = f.factory_name.name;
	
	var para = "field="+factory_no+"/"+factory_name;
	
	url = "../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&"+para;
	wopen(url,'enterCode','400','228','scrollbars=yes,toolbar=no,status=no,resizable=no');
}
</script>