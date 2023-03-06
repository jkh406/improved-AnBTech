<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkBR01.jsp"%>
<%@ page 
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.br.entity.*"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%!
	CarInfoTable table;
%>

<%
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("CarList");
	table = new CarInfoTable();
	Iterator table_iter = table_list.iterator();
%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../br/css/style.css" rel=stylesheet>
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
			  <TD valign='middle' class="title"><img src="../br/images/blet.gif" align="absmiddle"> 공용차량등록관리</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='400'><a href="javascript:add_new();"><img src="../br/images/bt_add_car.gif" border="0" align="absmiddle"></a> <a href="javascript:view_detail();"><img src="../br/images/bt_view_d.gif" border="0" align="absmiddle"></a></TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>

<!--리스트-->
  <TR height=100%>
    <TD vAlign=top><form name='listForm' style='magrgin:0'>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=40 align=middle class='list_title'>선택</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>차종</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>모델명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>차량번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>연료구분</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>구입일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>등록일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>현상태</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
<%
	while(table_iter.hasNext()){
		table = (CarInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle class='list_bg' height="25"><input type=checkbox name=checkbox value="<%=table.getCid()%>"></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getCarType()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getModelName()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getCarNo()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getFuelType()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getBuyDate()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRegDate()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getStat()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></TD>
			<TR><TD colSpan=17 background="../br/images/dot_line.gif"></TD></TR>
	<% 
		}  //while 

	%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE></form>
</BODY>
</HTML>


<script language = 'javascript'>
<!--
function modify() {
	f = document.listForm.checkbox;
	var cid;
	var s_count = 0;
    for(i=0;i<f.length;i++){
		if(f[i].checked){
			cid = f[i].value;
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("수정대상 차량을 선택한 후, 실행하십시오.");
	   return;
    }

    if(s_count > 1){
	   alert("한번에 하나의 차량정보만 선택 및 수정 가능합니다.");
	   return;
    }

	location.href = "../servlet/BookResourceServlet?category=car&mode=upd_car_info&cid="+cid;
}

function view_detail() {
	f = document.listForm.checkbox;
	var cid;
	var s_count = 0;
    for(i=0;i<f.length;i++){
		if(f[i].checked){
			cid = f[i].value;
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("상세보기대상 차량을 선택한후 실행하십시오.");
	   return;
    }

    if(s_count > 1){
	   alert("한번에 하나의 차량정보만 상세보기 가능합니다.");
	   return;
    }

	location.href = "../servlet/BookResourceServlet?category=car&mode=view_detail&cid="+cid;
}

function add_new() {
	location.href = "../servlet/BookResourceServlet?category=car&mode=write_car";
}
//-->
</script>