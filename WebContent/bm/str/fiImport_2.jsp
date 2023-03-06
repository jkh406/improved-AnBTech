<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "File Import 2 준비"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();

	//----------------------------------------------------
	//	파라미터 읽기
	//----------------------------------------------------
	String gid = (String)request.getAttribute("gid"); if(gid == null) gid = "";
	String model_code = (String)request.getAttribute("model_code"); 
	if(model_code == null) model_code = "";
	String parent_code = (String)request.getAttribute("parent_code"); 
	if(parent_code == null) parent_code = "";
	String level_no = (String)request.getAttribute("level_no"); if(level_no.length()==0) level_no = "0";
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";
	int tg = msg.indexOf("사용권한");		//사용권한 있는지 판단하기
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="eForm" method="post" style="margin:0" encType="multipart/form-data">
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD vAlign=top>
	<TABLE border=0 cellspacing=0 cellpadding=1 width="100%">
		<TBODY>
			<TR><TD height=25 style='padding-left:5px' class='bg_05' background='../bm/images/title_bm_bg.gif'>PART LIST IMPORT<!--타이틀--></TD></TR>
			<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
			<TR><TD height=23 align=left width='20%' style='padding-left:5px'  bgcolor=''><!--버튼-->
				<IMG src='../bm/images/sub_momitem_code.gif' align='absmiddle' border='0'>
				<INPUT type='text' name='' value='<%=parent_code%>' size='11' readonly>
					<A href='javascript:plImport();'><IMG src='../bm/images/bt_file_import.gif' align='absmiddle' border='0' alt='파일올리기'></A></TD></TD></TR>
			<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<!--내용-->
			<TR height=100%><TD vAlign=top>
				<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
				<TBODY>
					<TR><TD width="10%" height="25" class="bg_03" background="../bm/images/bg-01.gif">구분자</td>
						<TD width="90%" height="25" class="bg_04">
							<INPUT class=conB type="radio" name="ck" value="tab" checked>탭
							<INPUT class=conB type="radio" name="ck" value="semi">세미콜론
							<INPUT class=conB type="radio" name="ck" value="pause">쉼표
							<INPUT class=conB type="radio" name="ck" value="space">공백
							<INPUT class=conB type="radio" name="ck" value="etc">기타
							<INPUT class=conB type="text" name="etc" value="" size='2'></td></TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></td></tr>
					<TR><TD width="10%" height="25" class="bg_03" background="../bm/images/bg-01.gif" >파일명</td>
						<TD width="90%" height="25" class="bg_04">
						<INPUT type='file' name='file_name' size=42>
						<IMG src='../bm/images/public_info_q.gif' border='0' align='absmiddle' alt='파일형식' onmouseover="show('품목코드|공정코드|Location');" onmouseout="hide();"></td>
					</TR>
					<TR><TD height='1' bgcolor='#9DA8BA' colspan='10'></TD></TR>
			</TBODY></TABLE></TD></TR>

			<DIV id="lding" style="position:absolute;left:10px;top:150px;width:224px;height:150px;visibility:hidden;">
			<img src='../bm/images/loading8.gif' border='0' width='214' height='200'>
			</DIV>
	</TBODY></TABLE>
</TD></TR></TABLE>

<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='pid' value=''>
<INPUT type='hidden' name='gid' value='<%=gid%>'>
<INPUT type='hidden' name='parent_code' value='<%=parent_code%>'>
<INPUT type='hidden' name='level_no' value='<%=level_no%>'>
<INPUT type='hidden' name='model_code' value='<%=model_code%>'>

<% if(tg != -1) out.println("<INPUT type='hidden' name='msg' value='"+msg+"'>");
   else out.println("<INPUT type='hidden' name='msg' value=''>");
%>
</FORM>
<DIV id="saving" style="position:absolute;left:0px;top:0px;width:300px;height:50px;visibility:hidden;">
<TABLE width="800" border="0" cellspacing=1 cellpadding=1 bgcolor="">
	<TR><TD height="40" align="center" valign="middle">
	</TD> 
	</TR>
</TABLE>
</DIV>
<layer name="tooltip" visibility="hide"> 
<div id="tooltip" style="position:absolute; background:#eeeeee; Display:none; border:1; border-style:solid;"></div> 
</layer> 
</BODY>
</HTML>

<script language=javascript>
<!--
//메시지 출력하기
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
}
//P/L 올리기
function plImport()
{
	var msg = document.eForm.msg.value;
	if(msg.length != 0) { alert(msg); return;}
	
	var f = document.eForm.file_name.value;
	if(f.length == 0) { alert('파일을 선택하십시오.'); return; } 

	var gid = document.eForm.gid.value;
	if(gid.length == 0) { alert('품목코드를 선택하십시오.'); return; } 
	
	// 등록 파일이 엑셀 파일 및 txt 파일만 가능하도록 체크
	var access_file = document.eForm.file_name.value;
	var length = access_file.length;
	var ext_name = access_file.substring(access_file.lastIndexOf("."),length);
	if (ext_name == ".xls" || ext_name == ".txt"){
	} else {
		alert('엑셀 파일 또는 Text 파일만 등록 가능합니다.');
		return;
	}
	
	//데이터 저장하기
	parent.search.document.sForm.permit.value='N';		//검색창 disable
	document.all['lding'].style.visibility="visible";	//처리중 메시지 출력
	//document.all['saving'].style.visibility="visible";	//메뉴버튼 disable

	document.eForm.action='../servlet/BomInputServlet';
	document.eForm.mode.value='fi_import_2';
	document.onmousedown=dbclick;	// 데이터 전송시 다른 Action 방지 이벤트
	document.eForm.submit();
}

function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
}

ns4 = (document.layers)? true:false 
ie4 = (document.all)? true:false 

if ( (ns4) || (ie4) ) { 
     document.onmousemove = mouseMove 
     if (ns4) document.captureEvents(Event.MOUSEMOVE) 
} 

function mouseMove(e) { 
  if (ns4 && document.layers["tooltip"].visibility=="hide") { 
    document.layers["tooltip"].pageX = e.pageX; 
    document.layers["tooltip"].pageY = e.pageY 
  } 
} 

function show(val) { 
  if (ie4) { 
    document.all["tooltip"].innerText=val; 
    document.all["tooltip"].style.pixelLeft = event.clientX + document.body.scrollLeft; 
    document.all["tooltip"].style.pixelTop = event.clientY + document.body.scrollTop - 10; 
    document.all["tooltip"].style.display = ''; 
  } else { 
    val = '<table border=1 cellpadding=0 cellspacing=0 bgcolor=#eeeeee><tr><td><font size=2 face=굴림>' + val + '</font></td></tr></table>' 
    document.layers["tooltip"].document.open(); 
    document.layers["tooltip"].document.write(val); 
    document.layers["tooltip"].document.close(); 
    document.layers["tooltip"].visibility = "show"; 
  } 
} 

function hide() {  
  if (navigator.appName != 'Netscape' || document.layers == null) { 
    document.all["tooltip"].style.display = 'none'; 
  } else { 
    document.layers["tooltip"].visibility = "hide"; 
  } 
} 
-->
-->
</script>