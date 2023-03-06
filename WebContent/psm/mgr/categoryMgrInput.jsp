<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "과제 카테고리관리"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	//--------------------
	//	초기화 정보
	//--------------------
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	bean.openConnection();

	/*********************************************************************
	 	내부데이터 받기
	*********************************************************************/
	String query = "",msg="";
	String mode = request.getParameter("mode"); if(mode == null) mode = "";
	String pid = request.getParameter("pid"); if(pid == null) pid = ""; 
	String korea_name = Hanguel.toHanguel(request.getParameter("korea_name"));
	if(korea_name == null) korea_name=""; 
	String english_name = Hanguel.toHanguel(request.getParameter("english_name"));
	if(english_name == null) english_name=""; 
	String key_word = Hanguel.toHanguel(request.getParameter("key_word"));
	if(key_word == null) key_word=""; else key_word = key_word.toUpperCase();
	String comp_no = Hanguel.toHanguel(request.getParameter("comp_no"));
	if(comp_no == null) comp_no=""; 
	String comp_korea = Hanguel.toHanguel(request.getParameter("comp_korea"));
	if(comp_korea == null) comp_korea=""; 
	String comp_english = Hanguel.toHanguel(request.getParameter("comp_english"));
	if(comp_english == null) comp_english="예비"; 
	
	/*********************************************************************
	 	내용보기
	*********************************************************************/
	if(mode.equals("VIEW")) {
		query = "select * from psm_category where pid='"+pid+"'";
		bean.executeQuery(query);
		if(bean.next()) {
			pid = bean.getData("pid");
			korea_name = bean.getData("korea_name");
			english_name = bean.getData("english_name");
			key_word = bean.getData("key_word");
			comp_no = bean.getData("comp_no");
			comp_korea = bean.getData("comp_korea");
			comp_english = bean.getData("comp_english");
		}
	}
	String caption = "";
	if(mode.equals("ADD")){
		caption = "등록";
	} else if(mode.equals("VIEW")){
		caption = "정보";
	}
	
%>
<HTML>
<HEAD><TITLE>과제 카테고리<%=caption%> </TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>
<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0">
<FORM name="sForm" method="post" style="margin:0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../images/blet.gif"> 과제 카테고리<%=caption%></TD>
				</TR>
				</TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=27><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
					<%if(mode.equals("ADD")){%>
					<a href="javascript:write();"><img src="../images/bt_save.gif" border=0></a>
					<%}%>
					<%if(mode.equals("MOD") || mode.equals("VIEW")){%>
					<a href="javascript:contentModify();"><img src="../images/bt_modify.gif" border=0></a>
					<%}%>
					<%if(mode.equals("DEL") || mode.equals("VIEW")){%>
					<a href="javascript:contentDelete();"><img src="../images/bt_del.gif" border=0></a>
					<%}%>
					<a href="javascript:contentList();"><img src="../images/bt_list.gif" border=0></a>
					</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">카테고리한글명</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="korea_name" maxlength='25' value="<%=korea_name%>" size="20"></td>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">업체(한글)명</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="comp_korea" value="<%=comp_korea%>" size="20" maxlength='25' onclick="javascript:checkEdit();">
					<a href="javascript:selectComp();"><img src="../images/bt_search.gif" border="0" align='absmiddle'></a></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">카테고리영문명</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="english_name" maxlength='50' value="<%=english_name%>" size="20"></td>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">업체(영문)명</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="comp_english" value="<%=comp_english%>" size="20" readonly></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">영문약어(3자)</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="key_word" maxlength='3' value="<%=key_word%>" size="4"></td>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">업체사업자번호</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="comp_no" value="<%=comp_no%>" size="20" maxlength="30" onclick="javascript:checkEdit();"></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
	<TR><TD height='5'></TD></TR>
</table>
<input type="hidden" name="mode" value=''>
<input type="hidden" name="pid" value='<%=pid%>'>
</form> 
</body>
</html>

<script language=javascript>
<!--
var msg = '<%=msg%>';
if(msg.length != 0) {alert(msg); }
//등록하기
function write()
{
	var f = document.sForm;

	var korea_name = f.korea_name.value;
	if(korea_name == '') { alert('카테고리 한글명을 입력하십시오.'); f.korea_name.focus(); return; }
	var english_name = f.english_name.value;
	if(english_name == '') { alert('카테고리 영문명을 입력하십시오.'); f.english_name.focus(); return; }
	var key_word = f.key_word.value;
	if(key_word == '') { alert('카테고리 영문약어명을 입력하십시오.'); f.key_word.focus(); return; }
	else if(key_word.length != '3') { alert('카테고리 영문약어는 3자리 입니다.'); f.key_word.focus(); return; }
	var comp_korea = f.comp_korea.value;
	if(comp_korea == '') { alert('업체정보를 입력하십시오.'); return; }
	var comp_no = f.comp_no.value;
	if(comp_no == '') { alert('업체정보를 입력하십시오.'); return; }

	var pid = f.pid.value;
	if(pid != '') { alert('수정버튼을 이용하십시오.'); return; }

	document.sForm.action='categoryMgrProcess.jsp';
	document.sForm.mode.value='ADD';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//내용수정하기
function contentModify()
{
	var f = document.sForm;

	var korea_name = f.korea_name.value;
	if(korea_name == '') { alert('카테고리 한글명을 입력하십시오.'); f.korea_name.focus(); return; }
	var english_name = f.english_name.value;
	if(english_name == '') { alert('카테고리 영문명을 입력하십시오.'); f.english_name.focus(); return; }
	var key_word = f.key_word.value;
	if(key_word == '') { alert('카테고리 영문약어명을 입력하십시오.'); f.key_word.focus(); return; }
	else if(key_word.length != '3') { alert('카테고리 영문약어는 3자리 입니다.'); f.key_word.focus(); return; }
	var comp_korea = f.comp_korea.value;
	if(comp_korea == '') { alert('업체정보를 입력하십시오.'); return; }
	var comp_no = f.comp_no.value;
	if(comp_no == '') { alert('업체정보를 입력하십시오.'); return; }

	var pid = f.pid.value;
	if(pid == '') { alert('등록버튼을 이용하십시오.'); return; }

	document.sForm.action='categoryMgrProcess.jsp';
	document.sForm.mode.value='MOD';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//내용삭제하기
function contentDelete()
{
	d = confirm("삭제하시겠습니까?");
	if(d == false) return;

	document.sForm.action='categoryMgrProcess.jsp';
	document.sForm.mode.value='DEL';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//내용보기
function contentView(pid)
{
	document.vForm.action='categoryMgrInput.jsp';
	document.vForm.mode.value='VIEW';
	document.vForm.pid.value=pid;
	document.onmousedown=dbclick;
	document.vForm.submit();
}

// 목록보기
function contentList()
{
	document.sForm.action='categoryMgrList.jsp';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//업체정보 찾기
function selectComp()
{
	var strUrl = "../searchCompany.jsp?target=sForm.comp_no/sForm.comp_korea/sForm.comp_english";
	newWIndow = wopen(strUrl, "PsmCompInot", "260", "380", "scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//편집 제약조건 (예비인경우만 편집가능)
function checkEdit()
{
	var chk = document.sForm.comp_english.value;
	if(chk != "예비") document.sForm.comp_english.focus();
}
//창
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
//데이터 처리중 버튼막기
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>