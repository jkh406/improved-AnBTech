<%@ include file="../admin/configHead.jsp"%>
<%@ page
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.gm.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%!
	GoodsInfoTable table;
	GmLinkUrl redirect;
%>
<%
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ModelList");
	table = new GoodsInfoTable();
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
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../gm/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../gm/images/blet.gif" align="absmiddle"> 모델정보조회</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../gm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../gm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../gm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				  <form method=get action='../servlet/GoodsInfoServlet' name=srForm onSubmit="if(this.searchword.value.length< 2){alert('검색어는 2자이상 입력하셔야 합니다.');this.searchword.value='';this.searchword.focus();return false;}else{return true;}" style="margin:0">
					  <select name=searchscope onChange="">
						  <option value='code'>모델코드</option>
						  <option value='name'>한글모델명</option>
						  <option value='name2'>영문모델명</option>
					  </select>
					  <input type='text' name='searchword' size='10'>  
					  <input type="image" onfocus=blur() src="../gm/images/bt_search3.gif" border="0" align="absmiddle">
					  <a href="javascript:view_tree();"><img src="../gm/images/bt_search_tree.gif" border="0" align="absmiddle" alt="제품트리보기"></a>
					  <%=input_hidden_search%>
					</form>
					  </TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>모델코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>한글모델명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=180 align=middle class='list_title'>영문모델명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>최초등록일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>최종수정일자</TD>
		    </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (GoodsInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getGoodsCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:10px'><%=table.getGoodsName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:10px'><%=table.getGoodsName2()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRegisterDate()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getModifyDate()%></td>
			</TR>
			<TR><TD colSpan=11 background="../gm/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>

<script language='javascript'>

//상세검색
function search_detail() {

	var sParam = "src=search.jsp&frmWidth=600&frmHeight=500&title=search_detail";
	var category = document.srForm.category.value;
	var mode = document.srForm.mode.value;

	var sRtnValue=showModalDialog("../gm/modalFrm.jsp?"+sParam,"search","dialogWidth:600px;dialogHeight:500px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

	if (typeof sRtnValue != "undefined" && sRtnValue != "")
	{
		sParam = "&category="+category+"&searchscope=detail&searchword="+sRtnValue;
		location.href = "../servlet/ComponentApprovalServlet?mode="+mode + sParam;
	}
}	
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function view_tree()
{
	wopen('../gm/GoodsTreeMain.jsp','VIEW_TREE','324','457','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function make_tree()
{
	wopen('../servlet/GoodsInfoServlet?mode=make_tree','make_tree','5','5','scrollbars=no,toolbar=no,status=no,resizable=no');
}

</script>