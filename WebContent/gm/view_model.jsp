<%@ include file="../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.gm.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>

<%!
	GoodsInfoTable table;
	GoodsInfoItemTable spec;
	GmLinkUrl link;
%>

<%
	//모델기본정보 가져오기
	table = (GoodsInfoTable)request.getAttribute("ModelInfo");
	String mid			= table.getMid();
	String class_str	= table.getOneClass();
	String code			= table.getGoodsCode();
	String name			= table.getGoodsName();
	String name2		= table.getGoodsName2();
	String fg_code		= table.getFgCode()==""?"미채번":table.getFgCode();
	String register_info= table.getRegisterInfo();
	String register_date= table.getRegisterDate();
	String modifier_info= table.getModifierInfo();
	String modify_date	= table.getModifyDate();

	//세부스펙리스트 가져오기
	ArrayList spec_list = new ArrayList();
	spec_list = (ArrayList)request.getAttribute("SpecList");
	spec = new GoodsInfoItemTable();
	Iterator spec_iter = spec_list.iterator();

	//링크문자열 가져오기
	link = (GmLinkUrl)request.getAttribute("Redirect");
%>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="../gm/css/style.css">
</head>

<body topmargin="0" leftmargin="0" oncontextmenu="return false">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../gm/images/blet.gif"> 모델규격상세정보</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<a href="<%=link.getLinkList()%>"><IMG src='../gm/images/bt_list.gif' border='0' align='absmiddle'></a>
<%	// 권한 체크
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("GM01");
	if (idx >= 0){
%>
					<a href="<%=link.getLinkModify()%>"><IMG src='../gm/images/bt_modify.gif' border='0' align='absmiddle'></a>
					<a href="javascript:confirm_del('<%=link.getLinkDelete()%>');"><IMG src='../gm/images/bt_del.gif' border='0' align='absmiddle'></a>
					<a href="javascript:why_revision('<%=mid%>');"><IMG src='../gm/images/bt_model_branch.gif' border='0' align='absmiddle' alt="모델파생"></a>
<%	}	%>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">제품분류</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=class_str%></td></tr>
        <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">모델코드</td>
           <td width="37%" height="25" class="bg_04"><%=code%></td>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">F/G코드</td>
           <td width="37%" height="25" class="bg_04"><%=fg_code%></td></tr>
        <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">모델명(한글)</td>
           <td width="37%" height="25" class="bg_04"><%=name%></td>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">모델명(영문)</td>
           <td width="37%" height="25" class="bg_04"><%=name2%></td></tr>
        <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">최초등록자</td>
           <td width="37%" height="25" class="bg_04"><%=register_info%></td>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">최초등록일</td>
           <td width="37%" height="25" class="bg_04"><%=register_date%></td></tr>
        <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">최종수정자</td>
           <td width="37%" height="25" class="bg_04"><%=modifier_info%></td>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">최종수정일</td>
           <td width="37%" height="25" class="bg_04"><%=modify_date%></td></tr>
        <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
</tbody></table>

<table border=0 width='100%'><tr><td align=left><img src='../gm/images/title_model_spec.gif' border='0' alt="모델규격"></td></tr></table>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=50 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>항목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>데이터값</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<%
	int no = 1;
	while(spec_iter.hasNext()){
		spec = (GoodsInfoItemTable)spec_iter.next();
%>
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=no%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=spec.getItemName()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='left-padding:3 '><%=spec.getItemValue()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=spec.getItemUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'></td>
		</TR>
		<TR><TD colSpan=9 background="../gm/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
</TBODY></TABLE>
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

//모델파생
function why_revision(mid) {

	var url = "../gm/revision.jsp?mid=" + mid;

	wopen(url,'revision','350','139','scrollbars=no,toolbar=no,status=no,resizable=no');

}

function confirm_del(url){
	if (confirm("모델정보 삭제는 다른 모듈에 치명적인 영향을 미칠 수 있습니다. 정말로 삭제하시겠습니까?")){
		location.href = url; 
	}
}

</script>