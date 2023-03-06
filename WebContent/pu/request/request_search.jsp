<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%!
	RequestInfoTable table;
%>
<%
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("REQUEST_LIST");
	Iterator table_iter = table_list.iterator();
	
	//링크 문자열 가져오기
	com.anbtech.pu.entity.PurchaseLinkUrl redirect = new com.anbtech.pu.entity.PurchaseLinkUrl();
	redirect = (PurchaseLinkUrl)request.getAttribute("REDIRECT");
	
	String view_pagecut = redirect.getViewPagecut();
//	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
	String input_hidden = redirect.getInputHidden();

%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../pu/css/style.css" rel=stylesheet>
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>

<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif" align="absmiddle"> 구매요청현황</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../pu/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../pu/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../pu/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<TD align=left width='110' style="padding-left:5px">
				   <form method="get" action="../servlet/PurchaseMgrServlet" name="srForm" onSubmit="if(this.searchword.value.length< 2){alert('검색어는 2자이상 입력하셔야 합니다.');this.searchword.value='';this.searchword.focus();return false;}else{checkForm();}" style="margin:0">
					  <select name="searchscope" onChange="selSearchScope();">
						<option value='request_no'>구매요청번호</option>
						<option value='requester_div_name'>구매요청부서</option>
						<option value='requester_info'>구매요청자명</option>
						<option value='request_date'>구매요청일자</option>
					  </select></TD>
				<TD align=left width='500'>
					  <div id="sword" class="expanded" style="position:relative;"><table cellSpacing=0 cellPadding=0 width="100%" border='0'><tr><td>
						  <input type="text" name="searchword" size="10"> <input type="image" onfocus=blur() src="../mr/images/bt_search3.gif" border="0" align="absmiddle"></td></tr></table></div>
					  <div id="request_date" class="collapsed" style="position:relative;"><table cellSpacing=0 cellPadding=0 width="500" border='0'><tr><td>
						  <input type="text" name="s_date" size="8" maxlength="8"> ~ <input type="text" name="e_date" size="8" maxlength="8"> <a href="javascript:checkForm();"><img src="../mr/images/bt_search3.gif" border="0" align="absmiddle"></a> (예:20040210~20040225)</td></tr></table></div></TD><%=input_hidden%></form>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=50 align=middle class='list_title'>구분</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=110 align=middle class='list_title'>구매요청번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>관련과제명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>구매요청금액</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>구매요청일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>구매요청부서</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>구매요청자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>진행상태</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>
<%
	while(table_iter.hasNext()){
		table = (RequestInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=table.getRequestType()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRequestNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style="padding-left:5px"><%=table.getProjectName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style="padding-right:1px"><%=sp.getMoneyFormat(table.getRequestTotalMount(),"")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRequestDate()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRequesterDivName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRequesterInfo()%></td>
			  
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=cn.getStatus(table.getProcessStat())%></td>
			</TR>
			<TR><TD colSpan=15 background="../pu/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>



<script language='javascript'>

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//검색 체크
function checkForm(){
	var f = document.srForm;
    
	if(f.searchscope.value == 'request_date'){
		f.searchword.value = f.s_date.value + f.e_date.value;

		if(f.searchword.value.length != 16){
			alert("검색일자를 올바르게 입력하십시오.");
			return;
		}
	}else{
		if(f.searchword.value.length < 2){
			alert("검색어를 2자 이상 입력하십시오.");
			f.searchword.focus();
			return;
		}	
	}
	
	f.submit();
}

//검색필드 선택 처리
function selSearchScope(){
	var f = document.srForm;
    
	if(f.searchscope.value == 'request_date'){
		show('request_date');
		hide('sword');
	}else{
		hide('request_date');
		show('sword');
	}
}

// 선택된 레이어를 숨김
function hide( menuname )
{
  if (navigator.appName =="Netscape" ) {
	  document.layers[menuname].visibility="hide";
  } else {
	  document.all[menuname].className="collapsed"
   }
}

// 선택된 레이이를 보여줌
function show( menuname )
{
  if (navigator.appName =="Netscape" ) {
	   document.layers[menuname].visibility="show";
  } else {
	   document.all[menuname].className="expanded"
  }
}
</script>