<%@ include file="../admin/configPopUp.jsp"%>
<%@ page
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.gm.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%!
	GoodsInfoTable table;
	GmLinkUrl redirect;
	String[] treeinfo;
%>
<%
	
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ModelList");
	Iterator table_iter = table_list.iterator();

	redirect = new GmLinkUrl();
	redirect = (GmLinkUrl)request.getAttribute("Redirect");
	
	String view_pagecut = redirect.getViewPagecut();
	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
%>
<HTML><HEAD><TITLE>모델정보검색</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../gm/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display()'>

<TABLE height="100%" width="100%" cellSpacing=0 cellPadding=0>
	<TBODY>
		<!-- 타이틀 및 페이지 정보 -->
		<!--<TR height=27>
			<TD vAlign=top >
				<!--<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
					<TBODY>
					<TR>
					<TD style="padding-right:10px" align='right' valign='middle'  bgcolor="#73AEEF">
					<img src="../gm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../gm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../gm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>-->
		<TR height=32><!--버튼 및 페이징-->
			<TD vAlign=top>
				<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0' align='center'>
				<TBODY>
					<TR>
						<TD width=8>&nbsp;</TD>
						<TD align=left width='500'>
				<FORM method=get action='../servlet/GoodsInfoServlet' name=srForm onSubmit="if(this.searchword.value.length< 2){alert('검색어는 2자이상 입력하셔야 합니다.');this.searchword.value='';this.searchword.focus();return false;}else{return true;}" style="margin:0">
						<SELECT name=searchscope onChange="">
						  <OPTION value='name'>모델명</option>
						  <OPTION value='code'>모델코드</option>
						  <OPTION value='item_no'>F/G코드</option>
						</SELECT>
						<INPUT type='hidden' name=mode value='search_model_info'>
						<INPUT type='text' name='searchword' size='10'>  
						<INPUT type="image" onfocus=blur() src="../gm/images/bt_search3.gif" border="0" align="absmiddle">
					 
				</FORM>
				</TD>
				<TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR height=100%><!--리스트-->
		<TD vAlign=top>
		<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:50; overflow-x:auto; overflow-y:auto;">	
			<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 align=center>
			<TBODY>
				<TR height='2' bgcolor='#9CA9BA'><TD  colspan=14></TD></TR>
				<TR vAlign=middle height=23>
					<TD noWrap width=40 align=middle class='list_title'>번호</TD>
					<TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
					<TD noWrap width=100 align=middle class='list_title'>모델코드</TD>
					<TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
					<TD noWrap width=200 align=middle class='list_title'>모델명</TD>
					<TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
					<TD noWrap width=100 align=middle class='list_title'>F/G코드</TD>
					<TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
					<TD noWrap width=150 align=middle class='list_title'>제품명</TD>
					<TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
					
					<TD noWrap width=300 align=middle class='list_title'>Description</TD>				
				</TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=12></TD></TR>
<%
	int no = 1;
	int cur_page = Integer.parseInt(view_total) - (Integer.parseInt(view_boardpage)-1)*10;

	while(table_iter.hasNext()){
		treeinfo = new String[11];
		treeinfo = (String[])table_iter.next();



%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=cur_page%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="javascript:returnValue('<%=treeinfo[10]%>')"><%=treeinfo[6]%></a></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=treeinfo[7]%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=treeinfo[8]%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=treeinfo[3]%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=treeinfo[11]%></TD>		  
			</TR>
			<TR><TD colSpan=12 background="../gm/images/dot_line.gif"></TD></TR>
<%
		cur_page--;
	}
%>
		</TBODY></TABLE></DIV></TD></TR></TABLE></TBODY></TABLE>
</BODY>
</HTML>

<script language='javascript'>
// 페이지정보가 오른쪽으로 밀리지 않도록 처리
function display() { 

    var w = window.screen.width; 
    var h = window.screen.height; 
		
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//var div_h = h - 470;
	var div_h = c_h - 33;
	item_list.style.height = div_h;

}

function returnValue(str) {
	var fieldValue=str.split("|"); 
	parent.document.forms[0].one_class.value = fieldValue[0];
	parent.document.forms[0].one_name.value = fieldValue[1];
	parent.document.forms[0].two_class.value = fieldValue[2];
	parent.document.forms[0].two_name.value = fieldValue[3];
	parent.document.forms[0].three_class.value = fieldValue[4];
	parent.document.forms[0].three_name.value = fieldValue[5];
	parent.document.forms[0].four_class.value = fieldValue[6];
	parent.document.forms[0].four_name.value = fieldValue[7];
	parent.document.forms[0].fg_code.value = fieldValue[8];
	
	parent.return_value();
	}
</script>
