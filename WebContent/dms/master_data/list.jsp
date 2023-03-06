<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.dms.entity.*"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%!
	MasterTable table;
	LinkUrl redirect;
%>
<%
	//master_data 리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("MasterData_List");
	table = new MasterTable();
	Iterator table_iter = table_list.iterator();

	//링크 문자열 가져오기
	redirect = new LinkUrl();
	redirect = (LinkUrl)request.getAttribute("Redirect");
	String view_pagecut = redirect.getViewPagecut();
	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
	String where_category = redirect.getWhereCategory();
%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../dms/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../dms/images/blet.gif" align="absmiddle"> 기술문서 (<%=where_category%>)</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../dms/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../dms/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../dms/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='400'>
				  <form method=get action='AnBDMS' name=srForm onSubmit="if(this.searchword.value.length< 2){alert('검색어는 2자이상 입력하셔야 합니다.');this.searchword.value='';this.searchword.focus();return false;}else{return true;}" style="margin:0">
					  <select name="searchscope">
						  <option value='subject'>문서제목</option>
						  <option value='doc_no'>문서번호</option>
						  <option value='writer_s'>작성자명</option>
						  <option value='register_s'>등록자명</option>
						  <option value='model_code'>관련모델코드</option>
					  </select> 
					  <input type=text name=searchword size='10'>
					  <input type="image" onfocus=blur() src="../dms/images/bt_search3.gif" border="0" align="absmiddle">
					<%=input_hidden_search%></form>
					  </TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=100 align=middle class='list_title'>문서번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>문서제목</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>최종버젼</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>조회수</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>등록자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>등록일</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	//******************************
	//루프로 table내용을 내보내는 곳 loop  **
	//******************************
	while(table_iter.hasNext()){
	table = (MasterTable)table_iter.next();
	String doc_no = table.getDocNo();
	String category = table.getCategoryId();
	String subject = table.getSubject();
	String last_version = table.getLastVersion();
	String register = table.getRegister();
	String register_day = table.getRegisterDay();
	String stat = table.getStat();
	String hit = table.getHit();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=doc_no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'>&nbsp;<%=subject%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=last_version%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=hit%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=register%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=register_day%></td>
			</TR>
			<TR><TD colSpan=11 background="../dms/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>


<SCRIPT language=JavaScript>
totalCount = 1;
</SCRIPT>

<script language='javascript'>
<!--
//상세검색
function search_detail() {

	var sParam = "src=search.jsp&category_info=<%=where_category%>&frmWidth=600&frmHeight=500&title=search_detail";
	var category = document.srForm.category.value;
	var mode = document.srForm.mode.value;
	
	var sRtnValue=showModalDialog("../dms/techdoc_data/modalFrm.jsp?"+sParam,"search","dialogWidth:600px;dialogHeight:500px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

	if (typeof sRtnValue != "undefined" && sRtnValue != "")
	{
		sParam = "&category="+category+"&searchscope=detail&searchword="+sRtnValue;
		location.href = "../servlet/AnBDMS?mode="+mode + sParam;
//		alert(sParam);
	}
}


//모델 검색
function m_search() {
	var f = document.srForm;
	var sel = f.searchscope;

	if(sel.value == 'model_code'){
		var sParam = "src=../gm/SearchModelByTree.jsp&frmWidth=800&frmHeight=800&title=search";
		
		var sRtnValue=showModalDialog("../gm/modalFrm.jsp?"+sParam,"search","dialogWidth:800px;dialogHeight:600px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

		if (typeof sRtnValue != "undefined" && sRtnValue != "")
		{
			var model = sRtnValue.split("|")
			f.searchword.value = model[0];
			f.searchscope.value = 'model_code';
			f.submit();
		}else{
			f.searchscope.value = 'subject';
		}
	}
}
//-->
</script>