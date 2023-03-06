<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkGM01.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<jsp:useBean id="tree" class="com.anbtech.gm.business.makeGoodsTreeItems"/>

<%
	String mode		= request.getParameter("mode")==null?"":request.getParameter("mode"); // 추가/수정/삭제 모드
	String id		= request.getParameter("id");	// 관리번호
	String code		= request.getParameter("code");	// 분류코드
	String name		= Hanguel.toHanguel(request.getParameter("name")); //분류명
	String level	= request.getParameter("level");// 분류레벨
	
	if(level == null) level = "1";
	if(code == null) code = "";

	String query = "SELECT * FROM goods_structure WHERE glevel = '" + level + "' AND gcode LIKE '" + code + "%'";
	bean.openConnection();
	bean.executeQuery(query);

	int mid = request.getParameter("mid") == null?0:Integer.parseInt(request.getParameter("mid"));
	String whereStr = "";

%>

<HTML><HEAD><TITLE></TITLE>
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
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> 제품분류관리 <%=tree.getGoodsClassStr(mid,whereStr).equals("")?"":"("+tree.getGoodsClassStr(mid,whereStr)+")"%></TD>
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
			  <TD align=left width=100%><% if(!level.equals("1")){ %><IMG src="../images/bt_upper.gif" onclick="javascript:history.go(-1);" align="absmiddle" border="0" style="cursor:hand" alt="상위분류로 이동합니다."> <% } %><IMG src='../images/bt_add.gif' onclick="javascript:openWin('category_input.jsp?j=a&level=<%=level%>&ancestor=<%=mid%>')" align='absmiddle' border='0' style='cursor:hand' alt="현재분류에 새로운 제품분류를 등록합니다."> * 제품명을 클릭하면 하위분류로 이동합니다.</TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=50 align=middle class='list_title'>순번</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>제품코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>제품명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>제품설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>관리</TD>
		    </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<%	
	int no = 1;
	while(bean.next()){	

		String name1 = "<a href=javascript:viewSubList('"+bean.getData("mid")+"','"+level+"','"+bean.getData("gcode")+"')>"+bean.getData("name")+"</a>";
		String mod_url = "<a href=javascript:openWin('category_input.jsp?j=u&gcode="+bean.getData("gcode")+"')><img src='../images/lt_modify.gif' border='0'></a>";
		String del_url = "<a href=javascript:del_confirm('" + bean.getData("mid") + "');><img src='../images/lt_del.gif' border='0'></a>";
%>

			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("code")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=name1%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg' style="padding-left:10px"></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=mod_url%> <%=del_url%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
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
	wopen(url,'add','400','160','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function viewUpperList(mid,level,code)
{
	code = code.substring(0,code.length-2);
	level = parseInt(level) - 1;
	if(level == 0){
		alert('최상위(제품군)분류입니다.');
		return;
	}
	location.href = "category_list.jsp?mid="+mid+"&level="+level+"&code="+code;
}

function viewSubList(mid,level,code)
{
	level = parseInt(level) + 1;
	if(level == 4){
		alert('최하위(모델군)분류입니다.');
		return;
	}
	location.href = "category_list.jsp?mid="+mid+"&level="+level+"&code="+code;
}

function del_confirm(mid)
{
	var c = confirm("정말로 삭제하시겠습니까?");
	if(c) wopen('category_input_p.jsp?j=d&mid='+mid,'del','1','1','scrollbars=no,toolbar=no,status=no,resizable=no');
}
</script>