<%@ include file= "../../admin/configHead.jsp"%>
<%@ include file= "../../admin/chk/chkES02.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.es.geuntae.entity.*"
%>
<%!
	GeunTaeTable table;
	GeunTaeLink redirect;
%>

<%
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_List");
	table = new GeunTaeTable();
	Iterator table_iter = table_list.iterator();

	//링크 문자열 가져오기
	redirect = new GeunTaeLink();
	redirect = (GeunTaeLink)request.getAttribute("Redirect");
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
	String view_pagecut = redirect.getViewPagecut();
%>

<HTML><HEAD><TITLE>근태정보편집</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../es/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../es/images/blet.gif" align="absmiddle"> 근태정보편집</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'>
				  <img src="../es/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../es/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../es/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='200'><a href="javascript:chkval();"><img src="../es/images/bt_modify.gif" border="0" align="absmiddle"></a></TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>

<!--리스트-->
  <TR height=100%>
    <TD vAlign=top><form method="get" name="sForm" style="magring:0">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=30 align='middle' class='list_title'><input type="checkbox" name="checkbox"  disabled></TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>구분</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>근태사유</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>근태일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>근태대상자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>작성일시</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	//******************************
	//루프로 table내용을 내보내는 곳 loop  **
	//******************************
	while(table_iter.hasNext()){
	table = (GeunTaeTable)table_iter.next();
	String gt_id = table.getGtId();
	String ys_kind = table.getYsKind();
	String gt_purpose = table.getGtPurpose();
	String gt_period = table.getGtPeriod();
	String written_day = table.getWrittenDay();
	String user_info = table.getUserInfo();
	String fellow_nams = table.getFellowNames()==null?"":table.getFellowNames();
	String users = "<select><option>" + user_info + "</option>";
	StringTokenizer ids = new StringTokenizer(fellow_nams,";");
	for(int i=0;i<ids.countTokens();i++) {
		users+= "<option>" + ids.nextToken()+ "</option>";
	}	
	users += "</select>";
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><input type=checkbox name=checkbox value="<%=gt_id%>"></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=ys_kind%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=gt_purpose%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=gt_period%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=users%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=written_day%></TD>
			<TR><TD colSpan=11 background="../es/images/dot_line.gif"></TD></TR>
	<% 
		}  //while 

	%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE></form>
</BODY>
</HTML>


<script language = 'javascript'>
<!--
function chkval() {
	f = document.sForm.checkbox;
	var gt_id;
	var s_count = 0;

    for(i=0;i<f.length;i++){
		if(f[i].checked){
			gt_id = f[i].value;
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("수정할 대상을 선택한 후, 실행하십시오.");
	   return;
    }

    if(s_count > 1){
	   alert("한번에 한개씩만 선택 가능합니다.");
	   return;
    }
	
	wopen("../es/geuntae/modifyHyuGaInfo.jsp?gt_id="+gt_id,'modify_hyuga','300','220','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//-->
</script>