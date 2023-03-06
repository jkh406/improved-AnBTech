<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkCM01.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	String mode		= request.getParameter("mode")==null?"":request.getParameter("mode"); // 추가/수정/삭제 모드
	String id		= request.getParameter("id");	// 관리번호
	String code		= request.getParameter("code");	// 분류코드
	String name		= Hanguel.toHanguel(request.getParameter("name")); //표준품목명
	String desc		= Hanguel.toHanguel(request.getParameter("desc")); //품목설명
	String level	= request.getParameter("level");// 분류레벨
	
	if(level == null) level = "1";
	if(id == null) id = "0";

	String query = "SELECT * FROM item_class WHERE item_level = '" + level + "' AND item_ancestor = '" + id + "' ORDER BY mid";
	bean.openConnection();
	bean.executeQuery(query);
%>

<HTML><HEAD><TITLE>품목분류관리</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> 품목분류관리</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
			  <TD align=left width=100%><% if(!level.equals("1")){ %><IMG src="../images/bt_upper.gif" onclick="javascript:history.go(-1);" align="absmiddle" border="0" style="cursor:hand" alt="상위분류"> <% } %><IMG src='../images/bt_add.gif' onclick="javascript:openWin('category_input.jsp?j=a&level=<%=level%>&ancestor=<%=id%>')" align='absmiddle' border='0' style='cursor:hand'> * 품목분류명 클릭하면 하위분류로 이동합니다.</TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=50 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>품목분류코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>품목분류명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=400 align=middle class='list_title'>품목분류설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>관리</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		    </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%	
	int no = 1;
	while(bean.next()){	

		String name1 = "<a href=javascript:viewSubList('"+bean.getData("mid")+"','"+level+"','"+bean.getData("item_code")+"')>"+bean.getData("item_name")+"</a>";
		String mod_url = "<a href=javascript:openWin('category_input.jsp?j=u&id="+bean.getData("mid")+"')><img src='../images/lt_modify.gif' border='0'></a>";
		String del_url = "<a href=javascript:del_confirm('" + bean.getData("mid") + "');><img src='../images/lt_del.gif' border='0'></a>";
%>

			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("item_code")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=name1%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style="padding-left:10px"><%=bean.getData("item_desc")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'>
<%	if(!level.equals("1")){	out.print(mod_url + " " + del_url);	}%>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=11 background="../images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>

<script language=javascript>
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}


function openWin(url)
{
	wopen(url,'add','500','188','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function viewUpperList(id,level,code)
{
	code = code.substring(0,code.length-2);
	level = parseInt(level) - 1;
	if(level == 0){
		alert('최상위 분류입니다.');
		return;
	}
	location.href = "category_list.jsp?id="+id+"&level="+level+"&code="+code;
}

function viewSubList(id,level,code)
{
	level = parseInt(level) + 1;
	if(level == 4){
		alert('최하위 분류입니다.(최대 3단계 분류만 허용합니다.)');
		return;
	}
	location.href = "category_list.jsp?id="+id+"&level="+level+"&code="+code;
}

function del_confirm(id)
{
	var c = confirm("정말로 삭제하시겠습니까?");
	if(c) wopen('category_input_p.jsp?j=d&id='+id,'del','1','1','scrollbars=no,toolbar=no,status=no,resizable=no');
}
</script>