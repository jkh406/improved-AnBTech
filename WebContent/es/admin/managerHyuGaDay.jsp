<%@ include file= "../../admin/configHead.jsp"%>
<%@ include file= "../../admin/chk/chkES01.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.admin.entity.*"
%>
<jsp:useBean id="recursion" class="com.anbtech.admin.db.makeClassTree"/>

<%!
	UserInfoTable table;
%>

<%
	String year = request.getParameter("y");
	String kind = request.getParameter("k");
	String division = request.getParameter("div");

	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_List");
	table = new UserInfoTable();
	Iterator table_iter = table_list.iterator();
%>


<HTML><HEAD><TITLE></TITLE>
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
			  <TD valign='middle' class="title"><img src="../es/images/blet.gif" align="absmiddle"> 개인별 휴가잔량 관리</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='400'>
						<form method="get" name="sForm" action="../servlet/GeunTaeServlet" style="margin:0">
						<SELECT name='year' onChange="chkYear();">
							<OPTION value="">년도 선택</OPTION>
							<OPTION value="2003">2003년</OPTION>
							<OPTION value="2004">2004년</OPTION>
							<OPTION value="2005">2005년</OPTION>
							<OPTION value="2006">2006년</OPTION>
							<OPTION value="2007">2007년</OPTION>
							<OPTION value="2008">2008년</OPTION>
							<OPTION value="2009">2009년</OPTION>
							<OPTION value="2010">2010년</OPTION>
							<OPTION value="2011">2011년</OPTION>
						</SELECT>
					<%	if(!year.equals("")){	%>
							<script language='javascript'>
								document.sForm.year.value = '<%=year%>';
							</script>
					<%	}	%>
						<SELECT name='kind'>
							<OPTION value="HD_003">월차</OPTION>
							<OPTION value="HD_006">년차</OPTION>
						</SELECT>
					<%	if(!kind.equals("")){	%>
							<script language='javascript'>
								document.sForm.kind.value = '<%=kind%>';
							</script>
					<%	}	%>

						<SELECT name='division'>
							<%=recursion.viewComboByCode(0,0)%>
						</SELECT>
					<%	if(!division.equals("")){	%>
							<script language='javascript'>
								document.sForm.division.value = '<%=division%>';
							</script>
					<%	}	%>
						<input type='button' value='실행' onClick='javascript:go();'>
						<input type='hidden' name='mode' value='manager_hyuga_day'>
				  </form></TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>

<!--리스트-->
  <TR height=100%>
    <TD vAlign=top><form name='listForm' style='magrgin:0'>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=100% align=middle class='list_title'>부서명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>직급</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>이름</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>사번</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>근속년수</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>계획수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../es/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>수정</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%
	while(table_iter.hasNext()){
		table = (UserInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=table.getDivision()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getUserRank()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getUserName()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getUserId()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getContinuousYear()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><input type='text' value='<%=table.getHyuGaRestDay()%>' size='5' name="ID_<%=table.getUserId()%>"></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="javascript:modify('<%=table.getUserId()%>');"><img src='../es/images/lt_modify.gif' border='0' align='absmiddle'></a></TD>
			<TR><TD colSpan=13 background="../es/images/dot_line.gif"></TD></TR>
	<% 
		}  //while 

	%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE></form>
</BODY>
</HTML>

<script language='javascript'>
	function go(){
		var f = document.sForm;
		var year = f.year.value;
		var kind = f.kind.value;
		var division = f.division.value;
		location.href = "../servlet/GeunTaeServlet?mode=manager_hyuga_day&y="+year+"&k="+kind+"&div="+division;
//alert("../servlet/GeunTaeServlet?mode=manager_hyuga_day&y="+year+"&k="+kind+"&d="+division);
	}

	function chkYear(){
		var f = document.sForm;
		var year = f.year.value;
		var now = new Date();
		var now_year = now.getYear();
		if(year > now_year){
			alert("당해년도까지의 현황만 볼 수 있습니다.");
			f.year.value = now_year;
			return;
		}
	}

	function modify(id){
		var f = document.sForm;
		var year = f.year.value;
		var kind = f.kind.value;
		var num  = eval("document.listForm.ID_"+id+".value;");
		wopen("../es/admin/modifyHyuGaDay.jsp?year="+year+"&kind="+kind+"&id="+id+"&num="+num,'modify_hyuga','200','70','scrollbars=no,toolbar=no,status=no,resizable=no');
	}

	function wopen(url, t, w, h,st) {
		var sw;
		var sh;
		sw = (screen.Width - w) / 2;
		sh = (screen.Height - h) / 2 - 50;

		window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
	}
</script>