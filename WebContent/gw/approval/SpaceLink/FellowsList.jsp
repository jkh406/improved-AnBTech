<%@ include file= "../../../admin/configPopUp.jsp"%>
<%@ page 
	language	= "java"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../../admin/errorpage.jsp"
%>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"%>

<%
	String target = Hanguel.toHanguel(request.getParameter("target"));
	String fellows = Hanguel.toHanguel(request.getParameter("fellows"));
	
%>

<HTML>
<HEAD>
<META content="text/html;charset=euc-kr" http-equiv=content-type>
<LINK href="../../css/style.css" rel=stylesheet>

</HEAD>
<BODY topmargin="0" leftmargin=5 marginwidth=0>

<!-- 상단여백 -->
<TABLE><TR><TD height='5'></TD></TR></TABLE>
<!-- 테두리 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="120" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD>

	<!--본문-->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="223" align='center'>
		<TR height='25px;'>
			<TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;" BACKGROUND='../../images/title_bm_bg.gif'>
			<font color="#4D91DC"><b>선택된 임직원 리스트</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD height='4'></TD></TR>
		<TR><TD width="100%" height="130" valign="top" align='middle'>
			<!-- 공유 리스트 시작-->
			<form name="listForm" method="post" style="margin:0">
			<select name="user_list" multiple size="8">
			<OPTGROUP label='--------------------'>
		<%
			StringTokenizer list = new StringTokenizer(fellows,";");
			while(list.hasMoreTokens()) {
				String user = list.nextToken();
				out.println("<option value='"+user+";'>"+user+";</option>");
			}

		%>	</select></FORM></TD></TR>
		<TR><TD width="100%" height="25" valign='top' style='padding-left:10px;'>
				<a href='javascript:delSelected();'><img src='../../images/bt_del_sel.gif' border='0'></a></td></TR>
	</TABLE>

</TD></TR></TABLE><!--테두리 끝-->

<!-- 중간여백 -->
<TABLE><TR><TD height='7px;'></TD></TR></TABLE>

<!-- 테두리 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="70" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD colspan='5'>	

	<!-- 내용 -->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="223" align='left'>
		<TR height='25px;'><TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='../../images/title_bm_bg.gif' colspan='5'><font color="#4D91DC"><b>동행자 선택방법</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD width="100%" height="100" valign="middle" bgcolor="F5F5F5" style='padding-left:5px;padding-right:5px;'>
			<font color="565656">
				1. 동행하고자 하는 임직원을 검색화면<br>&nbsp;&nbsp;&nbsp;&nbsp;에서 검색한다.<br>
				2. 검색된 임직원의 이름을 클릭하여<br> &nbsp;&nbsp;&nbsp;&nbsp;리스트에 추가한다.<br>
				3. 선택완료 버튼을 눌러 종료한다.	
			</font>
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
		alert("삭제할 대상을 선택해 주십시오.");
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
		user_list += from.options[i].value + "\n";
	} //for


	parent.opener.document.<%=target%>.value = user_list;
	top.close();
}

function addUsers(item) // item == usr|A030003/대리/박동렬 , div|34/통신연구실
{
    var fromField=item.split("|");			//사용자(usr) or 부서(div) 구분	
    var type =  fromField[0];				//usr or div
	var user = fromField[1];				//나머지

	var fromField2=user.split("/");			// 사번/직급/이름
	var id;
	var name;
	var rank;

	if(type == "usr"){
		id = fromField2[0];
		rank = fromField2[1];
		name = fromField2[2];
	}else{
		id = fromField2[0];
		rank = "";
		name = fromField2[1];	
	}

	if(type == "div"){
		alert("부서단위 선택은 할 수 없습니다.");
		return;
	}

	var where_list = document.listForm.user_list;

	//중복 추가할 수 없게 처리
	var length = where_list.length;
	for(j=0;j<length;j++) {	
		if(where_list.options[j].value == user) {
			alert('[중복]이미 추가된 항목입니다.');
			return;
		}
	}
	//리스트에 추가
	var option0 = new Option(id+"/"+name+";",id+"/"+name+";");	//text,value
	where_list.options[length] = option0;
}

//-->
</script>