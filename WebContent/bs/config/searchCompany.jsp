<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*,com.anbtech.text.Hanguel"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%	
	/*****************************************************************************************
	 * sf	: 폼이름
	 * sid	: 거래 업체 아이디(관리번호)
	 * sname : 거래 업체명
	 * 이 페이지를 호출할 때에는 searchCompany.jsp?sf=폼이름&sid=아이디&sname=회사명 식으로 호출한다.
	 *****************************************************************************************/
	String sf	= request.getParameter("sf");
	String sid	= request.getParameter("sid")==null?"na":request.getParameter("sid");
	String sname	= request.getParameter("sname")==null?"na":request.getParameter("sname");;
	
	String search_item = request.getParameter("sItem")==null?"name":request.getParameter("sItem");
	String search_word = request.getParameter("sWord")==null?"*":request.getParameter("sWord");
	if(search_word != null) 
		search_word = new String(search_word.getBytes("ISO-8859-1"), "euc-kr");	

	String sql="";
	if(search_item.equals("name")){
		sql = "SELECT company_no,name_kor,name_eng,company_post_no,company_address,chief_name FROM company_customer WHERE name_kor LIKE '%"+search_word+"%' or name_eng LIKE '%"+search_word+"%' ORDER BY name_kor DESC";
	}else{
		sql = "SELECT company_no,name_kor,name_eng,company_post_no,company_address,chief_name FROM company_customer WHERE " + search_item + " LIKE '%"+search_word+"%' ORDER BY name_kor DESC";
	}
	bean.openConnection();	
	bean.executeQuery(sql);
%>

<HTML><HEAD><TITLE>거래업체찾기</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display()'>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td align="center">
		<!--타이틀-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
				<TR>
					<TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_bs_customer.gif" alt="거래업체찾기"></TD></TR>
				<TR>
					<TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
    <!--버튼-->
	<form name="sForm" action="searchCompany.jsp" method="post" style="margin:0">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
				<tr>
					<td width="400" height=25 colspan="4" align="left">
					<SELECT name='sItem'>
						<OPTION value='name'>업체명</OPTION>
						<OPTION value='chief_name'>대표자명</OPTION>
						<OPTION value='address'>주 소</OPTION>
					</SELECT> 
				<%	
					if(search_item !=null){	%>
					<script language='javascript'>
						document.sForm.sItem.value = '<%=search_item%>';
					</script>
				<%	}
				%>			
					<input type="text" name="sWord" size="15"> <a href='javascript:document.sForm.submit();'><img src='../images/bt_search3.gif' border='0' align='absmiddle'></a> <a href="javascript:self.close();"><img src="../images/bt_close.gif" border="0" align="absmiddle"></a></td></tr></tbody></table>
					<input type='hidden' name='sf' value='<%=sf%>'><input type='hidden' name='sid' value='<%=sid%>'><input type='hidden' name='sname' value='<%=sname%>'>
				</form>

		<TABLE height="100%" cellSpacing=0 cellPadding=0 width="99%" border=0 valign="top">
			<TBODY>
				<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
				<TR height=200><!--리스트-->
					<TD vAlign=top>
						<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:278; overflow-x:scroll; overflow-y:scroll;">
						<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
							<TBODY>
								<TR vAlign=middle height=23>
									<TD noWrap width=200 align=middle class='list_title'>거래업체명</TD>
									<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
									<TD noWrap width=100 align=middle class='list_title'>대표자명</TD>
									<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
									<TD noWrap width=100% align=middle class='list_title'>주소</TD>
								</TR>
								<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
<%	
					while(bean.next()){
					String name = "<a href=\"javascript:returnValue('"+bean.getData("company_no")+"','"+bean.getData("name_kor")+"/"+bean.getData("name_eng")+"');\">"+bean.getData("name_kor")+"</a>";
%>
								<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
									<TD align=middle height="24" class='list_bg'><%=name%></td>
								    <TD><IMG height=1 width=1></TD>
									<TD align=middle class='list_bg'><%=bean.getData("chief_name")%></td>
									<TD><IMG height=1 width=1></TD>
									<TD align=left class='list_bg'>&nbsp;<%=bean.getData("company_address")%></td>
								</TR>
								<TR><TD colSpan=5 background="../images/dot_line.gif"></TD></TR>
	<%				}
	%>
			</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>

		<!--꼬릿말-->
		<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
			<TBODY>
				<TR>
					<TD width="100%" height=5 colSpan=4></TD>
				</TR>
				<TR>
					<TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../images/close.gif" width="46" height="19" hspace="10" border="0"></a></TD>
				</TR>
				<TR>
					<TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
				</TR>
        </TBODY></TABLE></td></tr></table>
</BODY></HTML>

<script language="javascript">

function returnValue(rid,rname)
{
	opener.document.<%=sf%>.<%=sid%>.value = rid;
	opener.document.<%=sf%>.<%=sname%>.value = rname;
	//opener.reflect_supplyer();
	self.close();
}

function changeAll()
{
	var f = document.mForm;
	if(f.sItem.options[0].selected){
		f.sItem.value = "name";
		f.sWord.value = "";
		f.submit();
	}

}
function checkForm()
{
	var f = document.sForm;

	if(f.sWord.value.length < 2){
			alert("검색어를 2단어 이상 입력하십시오.");
			f.sWord.focus();
			return false;
	}
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 565;
	item_list.style.height = div_h;

}

</script>