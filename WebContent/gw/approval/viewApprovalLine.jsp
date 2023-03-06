<%@ include file="../../admin/configHead.jsp"%>
<%@ page language="java" import="java.sql.*,com.anbtech.text.Hanguel" 
    contentType = "text/html; charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>

<%
	String target = request.getParameter("target");
	String anypass = request.getParameter("anypass");			//자신을 결재선에 포함[외출,국내출장만]
	if(anypass == null) anypass = "";
%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">
<LINK href="../css/style.css" rel=stylesheet>
<title>전자결재결재선</title>
</head>

<BODY topmargin="0" leftmargin=7 marginwidth=0 oncontextmenu="return false">

<!-- 상단여백 -->
<TABLE><TR><TD height='5'></TD></TR></TABLE>
<!-- 테두리 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="120" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD>

	<!--본문-->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="225" align='center'>
		<TR height='25px;'>
			<TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;" BACKGROUND='../images/title_bm_bg.gif'>
			<font color="#4D91DC"><b>결재선 선택</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD height='4'></TD></TR>
		<TR><TD width="100%" height="130" valign="top" align='middle'>
			<!-- 공유 리스트 시작-->
		<form name="aForm" method="post" style="margin:0">
				<input type='radio' name='line_type' value='검토' checked>검토&nbsp;
				<input type='radio' name='line_type' value='협조'>협조&nbsp;
				<input type='radio' name='line_type' value='승인'>승인&nbsp;
				<input type='radio' name='line_type' value='통보'>통보<br>
			<select name="dec_app_line" multiple size="6" style="width:210px">
			
<%			//저장된 결재선에서 불러오기 처리
				String callD = Hanguel.toHanguel(request.getParameter("DATA"));	//결재선 데이터
				String callNum = Hanguel.toHanguel(request.getParameter("NO"));	//결재선 갯수
				int callN = 0;
				if(callNum != null){
					callN = Integer.parseInt(callNum);
				}
				if((callD != null) && (callNum != null)){
					for(int i=0; i< callN; i++) {
						int strN = callD.indexOf(";");
						String LD = callD.substring(0,strN);						//text내용
						String VD = LD.substring(LD.indexOf(" ")+1,LD.length())+";";	//value값(결재항목제외한 나머지로 입력형식에 맞춘다(;가 있는이유))
						out.println("<option value='"+VD+"'>" + LD + ";</option>");
						callD = callD.substring(strN+1,callD.length());
					} 
				} 
				%>	</select></FORM></TD></TR>
		<TR><TD width="100%" height="25" valign='top' style='padding-left:10px;'>
				<a href='javascript:delShare();'><img src='../images/bt_del_sel.gif' border='0'></a></td></TR>
	</TABLE>

</TD></TR></TABLE><!--테두리 끝-->

<!-- 중간여백 -->
<TABLE><TR><TD height='7px;'></TD></TR></TABLE>

<!-- 테두리 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="70" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD colspan='5'>	

	<!-- 내용 -->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="225" align='left'>
		<TR height='25px;'><TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='../images/title_bm_bg.gif' colspan='5'><font color="#4D91DC"><b>결재선 지정 방법</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD width="100%" height="100" valign="middle" bgcolor="F5F5F5" style='padding-left:3px;padding-right:3px;'>
			<font color="565656">
				1. 결재선을 선택한다.<br>
				2. 선택대상 임직원을 검색화면에서 <br>&nbsp;&nbsp;&nbsp;&nbsp;검색 후 검색한다.<br>
				3. 과정1,2를 반복하여 원하는 결재선<br> &nbsp;&nbsp;&nbsp;&nbsp;을 지정한다.<br>
				4. 선택완료 버튼을 눌러 종료한다.	
			</font>
			</TD></TR></TABLE>

</TD></TR></TABLE><!--테두리-->

</BODY>
</HTML>

<!-- ****************** 메시지 전달부분 ****************************** -->

<script Language = "Javascript">
<!--

function addUsers(item)
{
    var fromField=item.split("|")
    var type =  fromField[0];
	var user = fromField[1];
	var is_chief = fromField[2];

	var fromField2=user.split("/");

	var id;
	var name;
	var rank;

	if(type == "usr"){
		id = fromField2[0];
		rank = fromField2[1];
		name = fromField2[2];
	}else{
		id = fromField2[0];
		rank = "부서원";
		name = fromField2[1];	
	}


	var where_list = document.aForm.dec_app_line;
	var line_obj = document.aForm.line_type;
	var line_type;

	for(var k=0; k<line_obj.length; k++){
		if(line_obj[k].checked) line_type = line_obj[k].value;
	}

	//자신은 지정하지 못하게
	if(id == "<%=login_id%>" && is_chief == 'N'){
		alert("자신은 결재라인에 지정할 수 없습니다.");
		return;
	} else if(id == "<%=login_id%>" && is_chief == 'Y' && "<%=anypass%>".length == 0) {
		alert("자신은 결재라인에 지정할 수 없습니다.");
		return;
	}

	//통보가 아닌 경우 부서단위 선택 못하게
	if(type == "div" && line_type != "통보"){
		alert("통보외에는 부서단위 선택은 할 수 없습니다.");
		return;
	}

	//통보일 경우에는 통보->부서통보로 지정
	if(type == "div") line_type = "통보";

	//검토자와 승인자는 중복 지정 못하게
	var length = where_list.length;
	for(j=0;j<length;j++) {	
		if(where_list.options[j].value == line_type +" " + id + " " + name + " " + rank) {
			alert('[ERROR!!]'+line_type+'자가 중복되었습니다.');
			return;
		}

		if((line_type == "검토" || line_type == "승인") && where_list.options[j].value.indexOf(line_type) != -1)
		{
			alert(line_type + '자는 한명만 지정 가능합니다.');
			return;
		}
		
		//결재선을 올바로 지정했는지 체크
		if(line_type == "승인" && (where_list.options[j].value.indexOf("통보") != -1))
		{
			alert("결재선 지정이 잘못되었습니다. [검토]-[협조]-[승인]-[통보] 순으로 지정하십시오.");
			return;
		}

		if(line_type == "협조" && (where_list.options[j].value.indexOf("통보") != -1 || where_list.options[j].value.indexOf("승인") != -1))
		{
			alert("결재선 지정이 잘못되었습니다. [검토]-[협조]-[승인]-[통보] 순으로 지정하십시오.");
			return;
		}

		if(line_type == "검토" && (where_list.options[j].value.indexOf("통보") != -1 || where_list.options[j].value.indexOf("승인") != -1 || where_list.options[j].value.indexOf("협조") != -1))
		{
			alert("결재선 지정이 잘못되었습니다. [검토]-[협조]-[승인]-[통보] 순으로 지정하십시오.");
			return;
		}
	}

	//리스트에 추가
	var option0 = new Option(line_type + " " + id + " " + name + " " + rank,line_type + " " + id + " " + name + " " + rank);
	where_list.options[length] = option0;
}


//결재자 삭제하기 
function delShare()
{
	var data = "";					//select전체내용
	var num = document.aForm.dec_app_line.selectedIndex;
	if(num < 0){
		alert("삭제할 항목을 선택해 주십시오.");
		return;
	}

	if(!confirm("삭제하시겠습니까?"))
		return;

	document.aForm.dec_app_line.options[num] = null;
}

//결재선 넘겨주기 (확인)
function returnSelected()
{
	var from = document.aForm.dec_app_line;			//결재선 사번 이름 부서명의 object
	var data = "";									//넘겨줄 데이터

	var strList = "";
	var help_count = 0;	// 협조자 개수
	for(i=0;i<from.length;i++)
	{
		strList += from.options[i].text + "\n";
		if(strList.indexOf("협조") != -1) help_count++;
	} //for
	
	if(help_count > 10){
		alert("협조자는 10명 이상 지정할 수 없습니다."); 
		return;
	}

	//검사
	if(strList.length == 0) {alert("선택된 결재선이 없습니다."); return;}
	if(strList.indexOf("승인") == -1) {alert("승인자가 빠졌습니다."); return;}

//	alert(strList);
	parent.opener.document.<%=target%>.value = strList;
	top.close();
}

//결재선 저장하기 
function submitWin()
{
	var from = document.aForm.dec_app_line;			//결재선 사번 이름 부서명의 object
	var data = "";									//넘겨줄 데이터

	//전체내용 읽기
	var strList = "";
	for(i=0;i<from.length;i++)
	{
		strList += from.options[i].text+";";
	} //for
	wopen("eleApproval_lineSave.jsp?Lsave="+strList,"LineS","280","142","scrollbar=no,toolbar=no,status=no,resizable=no");
}

//결재선 불러오기 
function submitWinCall()
{
	wopen("eleApproval_line.jsp","LineC","480","230","scrollbar=no,toolbar=no,status=no,resizable=no");
}

//전달받은 결재선 반영하기 (from eleApproval_line.jsp)
function lineCall(no,data)
{
	var list = data.split(";");
	for(i=0; i<no; i++) {
		document.aForm.dec_app_line.options[i] = new Option(list[i]);
	}  
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

-->
</script>
