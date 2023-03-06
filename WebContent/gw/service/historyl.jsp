<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	contentType = "text/html; charset=KSC5601" 		
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.io.*,java.util.*,java.sql.*,java.util.StringTokenizer"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%	
	String ap_id = request.getParameter("ap_id")==null?"":request.getParameter("ap_id"); // 고객회사 사업자 등록번호
	String at_id = request.getParameter("at_id")==null?"":request.getParameter("at_id"); // 고객ID
	String search_item = request.getParameter("sItem")==null?"a.at_name":request.getParameter("sItem");
	String search_word = request.getParameter("sWord")==null?"":request.getParameter("sWord");
	if(search_word != null) search_word = new String(search_word.getBytes("ISO-8859-1"), "euc-kr");	

	String from = request.getParameter("from")==null?"":request.getParameter("from");
	String to	= request.getParameter("to")==null?"":request.getParameter("to");

	String sql = "";
	String sql2 = "";

	/*************************
	 * 검색 조건별 쿼리문 조합
	 * history_table  : a
	 * user_table		: d
	 *
	 * ap_id		: 고객회사 사업자등록번호
	 * at_id		: 고객의 관리번호(mid)
	 * search_item	: 검색항목(name,pname,subject)
	 * search_word	: 검색어
	 ************************/

	sql		= "SELECT a.ah_id,a.au_id,a.s_day,s_time,a.class,a.subject,a.umask,a.flag,a.ap_name 'pname',a.at_name 'name',b.name 'uname' FROM history_table a,user_table b WHERE a.au_id = b.id and " + search_item + " LIKE '%" + search_word + "%' and (a.flag = 'EF' or a.au_id = '"+login_id+"') order by a.ah_id desc";
	sql2	= "SELECT count(*) FROM history_table a,user_table b WHERE a.au_id = b.id and " + search_item + " LIKE '%" + search_word + "%' and (a.flag = 'EF' or a.au_id = '"+login_id+"')";

	if(!from.equals("") && !to.equals("")){
		sql		= "SELECT a.ah_id,a.au_id,a.s_day,s_time,a.class,a.subject,a.umask,a.flag,a.ap_name 'pname',a.at_name 'name',b.name 'uname' FROM history_table a,user_table b WHERE a.au_id = b.id and (a.s_day >= '"+from+"' and a.s_day <= '"+to+"') and " + search_item + " LIKE '%" + search_word + "%' and (a.flag = 'EF' or a.au_id = '"+login_id+"') order by a.ah_id desc";
		sql2	= "SELECT count(*) FROM history_table a,user_table b WHERE a.au_id = b.id and (a.s_day >= '"+from+"' and a.s_day <= '"+to+"') and " + search_item + " LIKE '%" + search_word + "%' and (a.flag = 'EF' or a.au_id = '"+login_id+"')";
	}


	bean.setQuery(sql,sql2);

	String row_no = request.getParameter("row_no")==null?"10":request.getParameter("row_no");
	bean.setRowNo(Integer.parseInt(row_no));
	String pageNo = request.getParameter("pageNo")==null?"1":request.getParameter("pageNo");	
	int ipage = 1;				
	if(pageNo!=null) ipage = Integer.parseInt(pageNo);	
	bean.setPage(ipage);

	bean.initWithQuery();

%>
<HTML>
<HEAD>
<META content="text/html;charset=euc-kr" http-equiv=content-type>
<LINK href="../css/style.css" rel=stylesheet>
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
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> 고객서비스이력</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><%=bean.getCurrentPage()%>/<%=bean.getLastPage()%> <img src="../images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='600'><form method="get" action="historyl.jsp" name="sForm" style="margin:0">
				<SELECT name='sItem'>
					<OPTION value='a.at_name' selected>고객 이름</OPTION>
					<OPTION value='a.ap_name'>고객사 이름</OPTION>
					<OPTION value='a.subject'>서비스 제목</OPTION>
				</SELECT>
			<%	if(!search_item.equals("")){	%>
					<script language='javascript'>
						document.sForm.sItem.value = '<%=search_item%>';
					</script>
			<%	}	%><INPUT size=10 name=sWord value="<%=search_word%>">
				
				<a href="javascript:document.sForm.submit();"><img src="../images/bt_search3.gif" border="0" align="absmiddle"></a>
				<a href="javascript:searchDetail();"><img src="../images/bt_search_d.gif" border="0" align="absmiddle"></a>
				<a href="historyi.jsp?j=a"><img src="../images/bt_new.gif" border="0" align="absmiddle"></a>
<!--			<a href="javascript:outputExcel();"><img src="../images/bt_excel.gif" border="0" align="absmiddle"></a>-->
				<a href="javascript:chkval();"><img src="../images/bt_sangsin.gif" border="0" align="absmiddle"></a></TD>
			  <TD width='' align='right' style="padding-right:10px">
				<%	if (bean.getCurrentPage() <= 1) {	%>
					<img src='../images/bt_previous.gif' border='0' align="absmiddle">
				<%	} else 	{	%>
					<a href='javascript:goPage(<%=bean.getCurrentPage()-1%>)'><img src='../images/bt_previous.gif' border='0' align="absmiddle"></a>
				<%	} if ((bean.getCurrentPage() != bean.getLastPage()) && (bean.getLastPage() != -1 )) { %>
					<a href='javascript:goPage(<%=bean.getCurrentPage()+1%>)'><img src='../images/bt_next.gif' border='0' align="absmiddle"></a>
				<%	} else 	{  %> 
					<img src='../images/bt_next.gif' border='0' align="absmiddle">
				<%	} %></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
<!--리스트-->
  <TR height=100%>
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=22>
			  <TD noWrap width=30 align=middle class='list_title'><input type=checkbox name=checkbox onClick="check(document.sForm.checkbox)"></TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>제목</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=160 align=middle class='list_title'>고객사명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>고객명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>방문자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>관리</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<% while(bean.isAvailable()) {

	String ah_id	= bean.getData("ah_id");
	String au_id	= bean.getData("au_id");
	String s_day	= bean.getData("s_day");
	String v_class	= bean.getData("class");
	String subject	= bean.getData("subject");
	String umask	= bean.getData("umask");
	String flag		= bean.getData("flag");
	String pname	= bean.getData("pname");
	int pname_len = pname.length();
	if(pname_len > 15) pname = pname.substring(0,15)+"...";
	String name		= bean.getData("name");
	String uname	= bean.getData("uname");

	String modify_url = "&nbsp;";
	if(au_id.equals(login_id) && !flag.equals("EE") && !flag.equals("EF"))
		modify_url = "<a href='historyu.jsp?j=u&ah_id="+ah_id+"'><img src=../images/lt_modify.gif align=absMiddle border=0 alt=수정></a>";

	String drop_url = "&nbsp;";
	if(au_id.equals(login_id) && !flag.equals("EE") && !flag.equals("EF"))
		drop_url = "<a href=javascript:drop('"+ah_id+"','"+umask+"');><img src=../images/lt_del.gif align=absMiddle border=0 alt=삭제></a>";

	String view_History = "<a href=javascript:wopen('viewHistory.jsp?ah_id="+ah_id+"','view','630','625');>["+v_class+"] "+subject+"</a>";

	s_day = s_day.substring(0,4)+ "-" + s_day.substring(4,6)+ "-" + s_day.substring(6,8);

	String chk = "&nbsp;";
	if(!flag.equals("EE") && !flag.equals("EF") && au_id.equals(login_id))
		chk = "<input type=checkbox name=checkbox value="+ah_id+">";
%>

			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=chk%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=view_History%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=pname%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=s_day%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=uname%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=modify_url%> <%=drop_url%></td>
			<TR><TD colSpan=13 background="../images/dot_line.gif"></TD></TR>
<% 
	}  //while 
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE></form>

<form name="delForm" method="post" action="historyp.jsp" encType="multipart/form-data">
  <input type=hidden name=ah_id>
  <input type=hidden name=umask>
  <input type=hidden name=j value=d>
</form>

<form name = "eForm" method = "get">
  <input type="hidden" name="ap_id" value="<%=ap_id%>">
  <input type="hidden" name="at_id" value="<%=at_id%>">

  <input type="hidden" name="sItem" value="<%=search_item%>">
  <input type="hidden" name="sWord" value="<%=search_word%>">
  <input type="hidden" name="pageNo" value="<%=pageNo%>">	

  <input type="hidden" name="from" value="<%=from%>">	
  <input type="hidden" name="to" value="<%=to%>">	
</form>

</body>
</html>

<script>
function goPage(pageNo)
{
	document.eForm.action = "historyl.jsp"
	document.eForm.pageNo.value = pageNo
	document.eForm.print_type.value = document.sForm.print_type.value;
	document.eForm.submit()

}

function changePrint()
{
	document.eForm.action = "historyl.jsp"
	document.eForm.print_type.value = document.sForm.print_type.value;
	document.eForm.submit()
}

function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}

function drop(ah_id,umask)
{
	if(confirm('정말로 삭제하시겠습니까?')){
		document.delForm.ah_id.value = ah_id;
		document.delForm.umask.value = umask;
		document.delForm.submit();
	}else{
		return;
	}
}

function searchDetail()
{
	
	var url = "searchDetail.jsp";
	wopen(url,'search','450','196');
}

function chkval() {
	var f = document.sForm.checkbox;
	var plid = "";
	var s_count = 0;
    for(i=1;i<f.length;i++){
		if(f[i].checked){
			plid += f[i].value+",";
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("상신할 문서를 선택한 후, 실행하십시오.");
	   return;
    }

	location.href = "../approval/module/service_FP_App.jsp?flag=SERVICE&plid="+plid;
}

var checkflag = false; 

function check(field) { 
	if (checkflag == false) { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = true; 
		} 
	checkflag = true; 
	}else { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = false; 
		} 
	checkflag = false; 
	} 
} 

function outputExcel()
{
	wopen('../report/reportServiceReq.jsp','ServiceE','500','330');
}
</script>