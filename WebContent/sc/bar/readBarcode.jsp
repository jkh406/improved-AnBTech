<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���ڵ� �Է���"		
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
<HEAD><TITLE>���ڵ� �Է���</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../sc/css/style.css" rel=stylesheet type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="eForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../sc/images/blet.gif">���ڵ����</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32<!--��ư-->
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

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">���ڵ� Type</td>
			   <td width="87%" height="25" class="bg_04">
					<select name="barcode_type" style=font-size:9pt;color="black"; onChange='addTypeNote();'>
					<%
						String[] type_type = {"","EAN13","EAN8","UPCA","CODE128"};
						String[] type_name = {"�� ��","EAN13","EAN8","UPCA","CODE128"};
						for(int i=0; i<type_name.length; i++) {
							out.print("<option value='"+type_type[i]+"'>");
							out.println(type_name[i]+"</option>");
						} 
					%></select>
					</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">���ڵ� ������</td>
			   <td width="87%" height="25" class="bg_04">
					<input  type='text' name='barcode' value=''> <span id='ad'></span></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">���ؿ���</td>
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
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">��¼���</td>
			   <td width="87%" height="25" class="bg_04">
					<input  type='text' name='barcode_count' value='1'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">���ȼ���</td>
			   <td width="87%" height="25" class="bg_04">
					<input  type='text' name='used_count' value='0'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">��ǰ��</td>
			   <td width="87%" height="25" class="bg_04">
					<input  type='text' name='goods_name' value=''></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">�Ϸù�ȣ</td>
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
		<marquee behavior="alternate">������ ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.</marquee>
	</td> 
	</tr>
</table>
</div>

</body>
</html>


<script language=javascript>
<!--
//����ϱ�
function sendSave()
{
	var a=document.eForm.barcode_type.selectedIndex; 
	var b=document.eForm.barcode_type.options[a].value;
	if(b.length == 0) {alert('���ڵ� Type�� �����Ͻʽÿ�.'); return; }

	var barcode = document.eForm.barcode.value; 
	if(barcode.length == 0) { alert('���ڵ嵥���Ͱ� �Էµ��� �ʾҽ��ϴ�.'); return; } 
	var c = checkBarcodeType(b,barcode); if(c == '1') return;

	var pa=document.eForm.paper_type.selectedIndex; 
	var pt=document.eForm.paper_type.options[pa].value;

	var barcode_count = document.eForm.barcode_count.value; 
	if(barcode_count.length == 0) { alert('���ڵ� ��¼����� �Էµ��� �ʾҽ��ϴ�.'); return; } 
	else if(barcode_count.search(/\D/) != -1) { alert('���ڵ� ��¼����� ���ڸ� �Է��� �����մϴ�.'); return;}

	var used_count = document.eForm.used_count.value; 
	if(used_count.length == 0) { alert('���ؿ��� ���ȼ����� �Էµ��� �ʾҽ��ϴ�.'); return; } 
	else if(used_count.search(/\D/) != -1) { alert('���ؿ��� ���ȼ����� ���ڸ� �Է��� �����մϴ�.'); return;}

	var goods_name = document.eForm.goods_name.value; 
	var serial_no = document.eForm.serial_no.value; 
	if(serial_no.length != 0 && serial_no.length != 12) { if(serial_no.search(/\D/) != -1) { alert('���ڸ� �Է��� �����մϴ�.'); return;} alert('�Ϸù�ȣ�� ����12�ڸ����� �Է��ؾ� �մϴ�.'); return; }

	var para = "&barcode_type="+b+"&barcode="+barcode+"&barcode_count="+barcode_count+"&used_count="+used_count+"&goods_name="+goods_name+"&serial_no="+serial_no+"&paper_type="+pt;	
	wopen('../servlet/pdfCtrlServlet?mode=read_barcode'+para,'barcode','400','290','scrollbars=no,toolbar=no,status=no,resizable=no');
	
	//document.eForm.action='../servlet/pdfCtrlServlet';
	//document.eForm.mode.value='read_barcode';
	//document.eForm.submit();
}
//barcode type�� note
function addTypeNote()
{
	var a=document.eForm.barcode_type.selectedIndex; var b=document.eForm.barcode_type.options[a].value;
	if(b == "EAN13") { ad.innerHTML = " <font color=blue>(�ݵ�� ����12�ڸ����� �Է��ؾ� �մϴ�.)</font>";
	}else if(b == "EAN8") { ad.innerHTML = " <font color=blue>(�ݵ�� ����7�ڸ����� �Է��ؾ� �մϴ�.)</font>";
	}else if(b == "UPCA") { ad.innerHTML = " <font color=blue>(�ݵ�� ����11�ڸ����� �Է��ؾ� �մϴ�.)</font>";
	}else if(b == "CODE128") { ad.innerHTML = " <font color=blue>(����[Ư����������]+���ڰ� ���յ� ���ڿ� 128�ڱ��� �Է°����մϴ�.)</font>";
	} else ad.innerHTML="";
	
}
//barcode type�� ����˻��ϱ�
//a:barcode type, b:�Է¹��ڵ尪
function checkBarcodeType(a,b) 
{
	var r='0'; var cnt=0;
	if(a == 'EAN13') { 	if(b.search(/\D/) != -1) { alert('���ڸ� �Է��� �����մϴ�.'); r='1'; return r;} else if(b.length != 12) { alert('�ݵ�� 12�ڸ� ���ڸ� �Է��ؾ� �մϴ�'); r='1'; return r; } }
	else if(a == 'EAN8') { 	if(b.search(/\D/) != -1) { alert('���ڸ� �Է��� �����մϴ�.'); r='1'; return r;} else if(b.length != 7) { alert('�ݵ�� 7�ڸ� ���ڸ� �Է��ؾ� �մϴ�'); r='1'; return r; } }
	else if(a == 'UPCA') { 	if(b.search(/\D/) != -1) { alert('���ڸ� �Է��� �����մϴ�.'); r='1'; return r;} else if(b.length != 11) { alert('�ݵ�� 11�ڸ� ���ڸ� �Է��ؾ� �մϴ�'); r='1'; return r; } }
	else if(a == 'CODE128') { for(i=0; i<b.length; i++) { if(b.charAt(i).search(/\d/) != -1) cnt = eval(cnt+1); else cnt = eval(cnt+2); } if(cnt > 16) { alert('���ؿ���A4�� �Է��ϱ����ؼ��� �ִ�16��(����:1,������ Ư������:2)���� �Է��ؾ� �մϴ�. [T.C: '+cnt+'��]'); r='1'; return r; }}
}
//â
function wopen(url, t, w, h,st) {
	var sw = (screen.Width - w) / 2;
	var sh = (screen.Height - h) / 2 - 50;
	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>