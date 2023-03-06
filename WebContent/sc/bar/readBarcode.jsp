<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "바코드 입력폼"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

%>

<HTML>
<HEAD><TITLE>바코드 입력폼</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../sc/css/style.css" rel=stylesheet type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="eForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../sc/images/blet.gif">바코드생성</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32<!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px'>
					<a href="javascript:sendSave();"><img src="../sc/images/bt_reg.gif" border=0></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	
</TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">바코드 Type</td>
			   <td width="87%" height="25" class="bg_04">
					<select name="barcode_type" style=font-size:9pt;color="black"; onChange='addTypeNote();'>
					<%
						String[] type_type = {"","EAN13","EAN8","UPCA","CODE128"};
						String[] type_name = {"선 택","EAN13","EAN8","UPCA","CODE128"};
						for(int i=0; i<type_name.length; i++) {
							out.print("<option value='"+type_type[i]+"'>");
							out.println(type_name[i]+"</option>");
						} 
					%></select>
					</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">바코드 데이터</td>
			   <td width="87%" height="25" class="bg_04">
					<input  type='text' name='barcode' value=''> <span id='ad'></span></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">폼텍용지</td>
			   <td width="87%" height="25" class="bg_04">
					<select name="paper_type" style=font-size:9pt;color="black"; >
					<%
						String[] paper_type = {"60/A4"};
						String[] paper_name = {"LS-310 60/A4"};
						for(int i=0; i<paper_type.length; i++) {
							out.print("<option value='"+paper_type[i]+"'>");
							out.println(paper_name[i]+"</option>");
						} 
					%></select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">출력수량</td>
			   <td width="87%" height="25" class="bg_04">
					<input  type='text' name='barcode_count' value='1'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">사용된수량</td>
			   <td width="87%" height="25" class="bg_04">
					<input  type='text' name='used_count' value='0'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">상품명</td>
			   <td width="87%" height="25" class="bg_04">
					<input  type='text' name='goods_name' value=''></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">일련번호</td>
			   <td width="87%" height="25" class="bg_04">
					<input  type='text' name='serial_no' value=''></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='upload_path' value='<%=upload_path%>'>
</form>

<div id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<table width="400" border="1" cellspacing=1 cellpadding=1 bgcolor="#ABDDE9">
	<tr><td height="50" align="center" valign="middle" class='subB'>
		<marquee behavior="alternate">데이터 처리중입니다. 잠시만 기다려 주십시요.</marquee>
	</td> 
	</tr>
</table>
</div>

</body>
</html>


<script language=javascript>
<!--
//등록하기
function sendSave()
{
	var a=document.eForm.barcode_type.selectedIndex; 
	var b=document.eForm.barcode_type.options[a].value;
	if(b.length == 0) {alert('바코드 Type을 선택하십시오.'); return; }

	var barcode = document.eForm.barcode.value; 
	if(barcode.length == 0) { alert('바코드데이터가 입력되지 않았습니다.'); return; } 
	var c = checkBarcodeType(b,barcode); if(c == '1') return;

	var pa=document.eForm.paper_type.selectedIndex; 
	var pt=document.eForm.paper_type.options[pa].value;

	var barcode_count = document.eForm.barcode_count.value; 
	if(barcode_count.length == 0) { alert('바코드 출력수량이 입력되지 않았습니다.'); return; } 
	else if(barcode_count.search(/\D/) != -1) { alert('바코드 출력수량은 숫자만 입력이 가능합니다.'); return;}

	var used_count = document.eForm.used_count.value; 
	if(used_count.length == 0) { alert('폼텍용지 사용된수량이 입력되지 않았습니다.'); return; } 
	else if(used_count.search(/\D/) != -1) { alert('폼텍용지 사용된수량은 숫자만 입력이 가능합니다.'); return;}

	var goods_name = document.eForm.goods_name.value; 
	var serial_no = document.eForm.serial_no.value; 
	if(serial_no.length != 0 && serial_no.length != 12) { if(serial_no.search(/\D/) != -1) { alert('숫자만 입력이 가능합니다.'); return;} alert('일련번호는 숫자12자리까지 입력해야 합니다.'); return; }

	var para = "&barcode_type="+b+"&barcode="+barcode+"&barcode_count="+barcode_count+"&used_count="+used_count+"&goods_name="+goods_name+"&serial_no="+serial_no+"&paper_type="+pt;	
	wopen('../servlet/pdfCtrlServlet?mode=read_barcode'+para,'barcode','400','290','scrollbars=no,toolbar=no,status=no,resizable=no');
	
	//document.eForm.action='../servlet/pdfCtrlServlet';
	//document.eForm.mode.value='read_barcode';
	//document.eForm.submit();
}
//barcode type별 note
function addTypeNote()
{
	var a=document.eForm.barcode_type.selectedIndex; var b=document.eForm.barcode_type.options[a].value;
	if(b == "EAN13") { ad.innerHTML = " <font color=blue>(반드시 숫자12자리까지 입력해야 합니다.)</font>";
	}else if(b == "EAN8") { ad.innerHTML = " <font color=blue>(반드시 숫자7자리까지 입력해야 합니다.)</font>";
	}else if(b == "UPCA") { ad.innerHTML = " <font color=blue>(반드시 숫자11자리까지 입력해야 합니다.)</font>";
	}else if(b == "CODE128") { ad.innerHTML = " <font color=blue>(영문[특수문자포함]+숫자가 조합된 문자열 128자까지 입력가능합니다.)</font>";
	} else ad.innerHTML="";
	
}
//barcode type별 내용검사하기
//a:barcode type, b:입력바코드값
function checkBarcodeType(a,b) 
{
	var r='0'; var cnt=0;
	if(a == 'EAN13') { 	if(b.search(/\D/) != -1) { alert('숫자만 입력이 가능합니다.'); r='1'; return r;} else if(b.length != 12) { alert('반드시 12자리 숫자를 입력해야 합니다'); r='1'; return r; } }
	else if(a == 'EAN8') { 	if(b.search(/\D/) != -1) { alert('숫자만 입력이 가능합니다.'); r='1'; return r;} else if(b.length != 7) { alert('반드시 7자리 숫자를 입력해야 합니다'); r='1'; return r; } }
	else if(a == 'UPCA') { 	if(b.search(/\D/) != -1) { alert('숫자만 입력이 가능합니다.'); r='1'; return r;} else if(b.length != 11) { alert('반드시 11자리 숫자를 입력해야 합니다'); r='1'; return r; } }
	else if(a == 'CODE128') { for(i=0; i<b.length; i++) { if(b.charAt(i).search(/\d/) != -1) cnt = eval(cnt+1); else cnt = eval(cnt+2); } if(cnt > 16) { alert('폼텍용지A4에 입력하기위해서는 최대16자(숫자:1,영문및 특수문자:2)까지 입력해야 합니다. [T.C: '+cnt+'자]'); r='1'; return r; }}
}
//창
function wopen(url, t, w, h,st) {
	var sw = (screen.Width - w) / 2;
	var sh = (screen.Height - h) / 2 - 50;
	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>