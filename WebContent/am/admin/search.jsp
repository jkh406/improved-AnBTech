<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "�� �˻�"		
	contentType = "text/html; charset=euc-kr"
	import = "com.anbtech.text.Hanguel"
	errorPage = "../../admin/errorpage.jsp" 
%>

<%
	String category_full = Hanguel.toHanguel(request.getParameter("category_full"));
	if(category_full.equals("")||category_full.equals("&nbsp;")||category_full.equals("?")) {category_full="��ü ī�װ�";}
	String c_no = request.getParameter("c_no");
%>

<HTML><HEAD><TITLE>�󼼰˻�</TITLE></HEAD>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<form name="searchForm">
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
	<!--Ÿ��Ʋ-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
			<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
			<TR>
				<TD height="33" width='100%' valign="middle" bgcolor="#73AEEF"><img src="../images/pop_search_d.gif" width="181" height="17" hspace="10"></TD></TR>
			<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

		<TABLE cellspacing=0 cellpadding=2 width="98%" border=0>
			<TBODY>
			<TR><TD height=20 colspan="2"></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR>
				<TD width="17%" height="25" class="bg_01" background="../images/bg-01.gif">�ڻ�з�</TD>
				<TD width="83%" height="25" class="bg_02"><%=category_full%><input type="hidden" name="ct_name_full" size="60" value='<%=category_full%>' readonly></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR>
				<TD width="17%" height="25" class="bg_01" background="../images/bg-01.gif">�𵨸�</TD>
				<TD width="83%" height="25" class="bg_02"><input type="text" name="model_name" size="20"></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="2"></TD></TR>
			<TR>
				<TD width="17%" height="25" class="bg_01" background="../images/bg-01.gif">��������</TD>
				<TD width="83%" height="25" class="bg_02"><input type="text" name="s_day" size="8"> ~ <input type="text" name="e_day" size="8" > (��:20031225 ~ 20021225)</TD></TR>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="2"></TD></TR>
			<TR>
				<TD width="17%" height="25" class="bg_01" background="../images/bg-01.gif">�ڻ갡ġ</TD>
				<TD width="83%" height="25" class="bg_02"><input type="text" name="as_value1" size="10">�� ~ <input type="text" name="as_value2" size="10">�� (��:100000��~1000000��)</TD></TR>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="2"></TD></TR>
			<TR>
				<TD width="17%" height="25" class="bg_01" background="../images/bg-01.gif">����ڸ�</TD>
				<TD width="83%" height="25" class="bg_02"><input type="text" name="crr_name" size="10"></TD></TR>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="2"></TD></TR>
			<TR>
				<TD width="17%" height="25" class="bg_01" background="../images/bg-01.gif">�μ���</TD>
				<TD width="83%" height="25" class="bg_02"><input type="text" name="crr_rank" size="10"></TD></TR>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="2"></TD></TR>
			<TR><TD height=20 colspan="2"></TD></TR></TBODY></TABLE>

	<!--������-->
		<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
			<TR>
				<TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
					<IMG src="../images/bt_search3.gif" onClick="javascript:go();" border='0' style='cursor:hand' align='absmiddle'> <a href='javascript:self.close()'><img src='../images/bt_close.gif' border='0' align='absmiddle'></a></TD></TR>
			<TR><TD width="100%" height=3 bgcolor="0C2C55"></TD></TR>
        </TBODY></TABLE></form></TD></TR></TABLE></BODY></HTML>
<script>

function go() {

		var f = document.searchForm;

		var model_name = f.model_name.value;
		var s_day = f.s_day.value;
		var e_day = f.e_day.value;
		var day = s_day + e_day;

		var as_value1 = f.as_value1.value;
		var as_value2 = f.as_value2.value;
		var as_value = as_value1 + as_value2;

		var crr_name = f.crr_name.value;
		var crr_rank = f.crr_rank.value;

		var model_name_and = 'and';
		var s_day_and = 'and';
		var e_day_and = 'and';
		var as_value_and = 'and';
		var crr_name_and = 'and';
		var crr_rank_and = 'and';

		var str = '';
		if(model_name != '') str += model_name_and + "|model_name|" + model_name + ",";
		if(s_day != '') str += s_day_and + "|s_date|" + s_day + ",";
		if(e_day != '') str += e_day_and + "|e_date|" + e_day + ",";
		if(as_value1 != '') str += as_value_and + "|as_value1|"+as_value1+",";
		if(as_value2 != '') str += as_value_and + "|as_value2|"+as_value2+",";
		if(crr_name != '') str += crr_name_and + "|crr_name|"+crr_name+",";
		if(crr_rank != '') str += crr_rank_and + "|crr_rank|"+crr_rank+",";
		

		if( (f.s_day.value=="" && f.e_day.value!="") || (f.s_day.value!="" && f.e_day.value=="") ){
			
				if(f.s_day.value=="") { 
					f.s_day.focus(); 
				} else { 
					f.e_day.focus();
				}
			alert("�˻� ��¥�� ������ ��Ȯ�ϰ� ������ �ֽʽÿ�");
			return;

		}

		if( (f.as_value1.value=="" && f.as_value2.value!="") || (f.as_value1.value!="" && f.as_value2.value=="") ){
			alert("�˻� �ݾ��� ������ ��Ȯ�ϰ� ������ �ֽʽÿ�");
			if(f.as_value1.value=="") { f.as_value1.focus(); 
			} else { f.as_value2.focus();}
			return;
		}
		
		var sday = f.s_day.value;
		var eday = f.e_day.value;
		var value1 = f.as_value1.value;
		var value2 = f.as_value2.value;

		// ���Թ��ڰ� ���� ���� ��ȿ�� üũ & 8�� ��Ȯ�ϰ� �����ش��� üũ
		if(sday!="") 
		{
			if(!isNumber(sday)) { 
				alert("���ڷθ� �����ؾ� �մϴ�.");
				return;
			} 
			
			if(f.s_day.value.length!=8) {
				alert("��¥ 8�ڸ��� ��Ȯ�ϰ� ������ �ֽʽÿ�(��:20030102)");
				f.s_day.focus();
				return
			}
		}

		// ���Թ��ڰ� ���� ���� ��ȿ�� üũ & 8�� ��Ȯ�ϰ� �����ش��� üũ
		if(eday!="") 
		{
			if(!isNumber(eday)) { 
				alert("���ڷθ� �����ؾ� �մϴ�.");
				return;
			}

			if(f.e_day.value.length!=8) {
				alert("��¥ 8�ڸ��� ��Ȯ�ϰ� ������ �ֽʽÿ�(��:20030102)");
				f.e_day.focus();
				return
			}
		}

		// ���Աݾ��� �������� üũ
		if(value1!="") 
		{
			if(!isNumber(value1)) { 
			alert("���ڷθ� �����ؾ� �մϴ�.");
			return;
			}
		}
		
		// ���Աݾ��� �������� üũ
		if(value2!="") 
		{
			if(!isNumber(value2)) { 
			alert("���ڷθ� �����ؾ� �մϴ�.");
			return;
			}
		}

	//window.returnValue = str;
	opener.location.href = "../../servlet/AssetServlet?mode=asset_list&div=detail&searchscope=detail&searchword="+str+"&c_no="+'<%=c_no%>';
	self.close();
	}

	// ���� ��ȿ�� �˻�
	function isNumber(input) {
		var chars = "0123456789";
		return containsCharsOnly(input,chars);
		}

		function containsCharsOnly(input,chars) {
		for (var inx = 0; inx < input.length; inx++) {
		   if (chars.indexOf(input.charAt(inx)) == -1)
			   return false;
		}
		return true;
	}
</script>