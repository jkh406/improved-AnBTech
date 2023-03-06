<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,java.util.StringTokenizer"%>

<%
	String target = request.getParameter("target");
%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">
<LINK href="../css/style.css" rel=stylesheet>
<title>메일수신인리스트</title>
</head>

<BODY topmargin="0" leftmargin=0 marginwidth=0>

<!-- 상단여백 -->
<TABLE><TR><TD height='5'></TD></TR></TABLE>
<!-- 테두리 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="120" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD>

	<!--본문-->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="223" align='left'>
		<TR height='25px;'>
			<TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;" BACKGROUND='../images/title_bm_bg.gif'>
			<font color="#4D91DC"><b>현재 선택된 사원 리스트</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD height='4'></TD></TR>
		<TR><TD width="100%" height="130" valign="top" align='middle'>
			<!-- 공유 리스트 시작-->

		<form name="listForm" method="post" style="margin:0">
		<select size="8" name="user_list" multiple>
			<OPTGROUP label='--------------------'>
<% 				//지정된 수신인 불러오기 처리
				String Rec = Hanguel.toHanguel(request.getParameter("Rec"));
				if(Rec == null) Rec = "";
								
				StringTokenizer Receivers = new StringTokenizer(Rec,";");
				while(Receivers.hasMoreTokens()) {
					String rec = Receivers.nextToken()+";";
					out.println("<option value='"+rec+"'>" + rec + "</option>");
				}
%>		</select></form></TD></TR>
		<TR><TD width="100%" height="25" valign='top' style='padding-left:10px;'>
				<a href='javascript:delSelected();'><img src='../images/bt_del_sel.gif' border='0'></a></td></TR>
	</TABLE>
</TD></TR></TABLE><!--테두리 끝-->

<!-- 중간여백 -->
<TABLE><TR><TD height='7px;'></TD></TR></TABLE>

<!-- 테두리 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="70" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD colspan='5'>	

	<!-- 내용 -->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="223" align='left'>
		<TR height='25px;'><TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='../images/title_bm_bg.gif' colspan='5'><font color="#4D91DC"><b>메일수신인 지정 방법</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD width="100%" height="100" valign="middle" bgcolor="F5F5F5" style='padding-left:5px;padding-right:5px;'>
			
			<font color="565656">
				1. 공유하고자 하는 사원을 검색화면<br>&nbsp;&nbsp;&nbsp;&nbsp;에서 검색한다.<br>
				2. 검색된 사원의 이름을 클릭하여<br> &nbsp;&nbsp;&nbsp;&nbsp;공유 리스트에 추가한다.<br>
				3. 선택완료 버튼을 눌러 종료한다.</font>
			</TD></TR></TABLE>

</TD></TR></TABLE><!--테두리-->

</BODY>
</HTML>

<script language="javascript">
<!--
//리스트에서 항목 삭제하기
function delSelected()
{
	var num = document.listForm.user_list.selectedIndex;
	if(num < 0){
		alert("삭제할 사람을 선택해 주십시오.");
		return;
	}

	var Frm = document.listForm.user_list;
	var len = Frm.length;
	for (i=len-1;i>=0 ;i--) {
        if(Frm.options[i].selected == true) Frm.options[i] = null;
    }	
}

//리스트에 있는 내용 opener 에 보내기
function transferList()
{
	var from = document.listForm.user_list;

	var user_list = "";
	for(i=0;i<from.length;i++)
	{
		user_list += from.options[i].value + '\n';
	} //for


	parent.opener.document.<%=target%>.value = user_list;
	top.close();
}

function addUsers(item)
{
    var fromField=item.split("|")
    var type =  fromField[0];
	var user = fromField[1];

	var fromField2=user.split("/");
	var id = fromField2[0];
	var rank = fromField2[1];
	var name = fromField2[2];

	user = id + "/" + name;

	if(type == "div"){
		alert("부서단위 선택은 할 수 없습니다.");
		return;
	}

	var where_list = document.listForm.user_list;

	//중복 추가할 수 없게 처리
	var length = where_list.length;
	for(j=0;j<length;j++) {	
		if(where_list.options[j].value == user + ";") {
			alert('[중복]이미 추가된 항목입니다.');
			return;
		}
	}
	//리스트에 추가
	var option0 = new Option(user,user+";");
	where_list.options[length] = option0;
}

//-->
</script>