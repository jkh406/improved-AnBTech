<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "AS���� ���(��ü��)"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mr.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%

	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();

	//---------------------------------------------------------
	//	�Ķ���� �ޱ�
	//--------------------------------------------------------
	String company_no="",sItem="",sWord="";
	com.anbtech.mr.entity.assupportTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");

	para = new assupportTable();
	Iterator para_iter = para_list.iterator();

	if(para_iter.hasNext()) {
		para = (assupportTable)para_iter.next();
		company_no = para.getCompanyNo();			//��ü�ڵ�
		sItem = para.getSitem();		
		sWord = para.getSword();
	}

	//---------------------------------------------------------
	//	AS������� ����
	//--------------------------------------------------------
	com.anbtech.mr.entity.assupportTable field;
	ArrayList field_list = new ArrayList();
	field_list = (ArrayList)request.getAttribute("FIELD_List");
	field = new assupportTable();
	Iterator field_iter = field_list.iterator();
	int field_cnt = field_list.size();

	String[][] asfield = new String[field_cnt][6];
	int e = 0;
	while(field_iter.hasNext()) {
		field = (assupportTable)field_iter.next();
		asfield[e][0] = field.getRegisterName();		//�����̸�
		asfield[e][1] = field.getSno();					//�������ǥ��
		e++;
	}

%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../mr/css/style.css" type="text/css">
<script language='javascript'>
<!--
//�����Է��ϱ�
function saveResult()
{
	var request_name = sForm.request_name.value;
	if(request_name.length == 0) { alert("��û�ڰ� �Էµ��� �ʾҽ��ϴ�."); return; }

	//��û���ڿ� �������ڸ� ���Ͽ� 1���̳��������� �ľ��ϱ�
	var request_date = sForm.request_date.value;
	var as_date = sForm.as_date.value;
	if(request_date > as_date) { alert('�湮���ڰ� ��û���ں��� �����ϴ�.'); return; }

	var as_content = sForm.as_content.value;
	if(as_content.length == 0) { alert("A/S������ �Էµ��� �ʾҽ��ϴ�."); return; }
	
	//������ 2���̻��̸� �������� �Է°˻�
	if(compdate(request_date,as_date)) {
		var as_delay = sForm.as_delay.value;
		if(as_delay.length == 0) { 
			alert('��û�ϰ� �湮���� 2���̻��̸� \n\n���������� �Է��ϼž� �մϴ�.'); return; }
	}
	
	//��û��,�����ϳ��ڿ��� - ����
	for(i=0; i<2; i++) request_date = request_date.replace('-','');
	for(i=0; i<2; i++) as_date = as_date.replace('-','');

	document.sForm.action='../servlet/asresultworkServlet';
	document.sForm.mode.value='ART_W';
	document.sForm.request_date.value=request_date;
	document.sForm.as_date.value=as_date;
	document.sForm.submit();
}
//�Է�����ϱ�
function cancelResult()
{
	document.sForm.action='../servlet/asresultworkServlet';
	document.sForm.mode.value='ART_L';
	document.sForm.submit();
}

//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../mr/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
}

//���ں��ϱ�
function compdate(a,b)
{
	//a���� +2���� ���� Date�� �ٽ� Setting�ϱ�
	var syear	= a.substring(0, 4);			
    var smonth	= a.substring(5, 7) ;			
    var sday	= a.substring(8, 10);			
		sday = eval(eval(sday)+2);			//2���� �ʰ��ϴ��� �Ǵ��ϱ� ����
	var days = DaysInMonth(eval(smonth),syear);
	if(sday > days) {
		smonth = eval(eval(smonth)+1);
		sday = eval(sday - days);			//�� ���ϱ�
		if(smonth > 12) {
			smonth = eval(smonth - 12);		//�� ���ϱ�
			syear = eval(syear + 1);		//�� ���ϱ�
		}
	}
    now = new Date();
    now.setTime(Date.UTC(syear, smonth, sday))

	//setting�� ���ڱ��ϱ�
	var nyear	= now.getYear();
	var nmonth	= now.getMonth();	if(nmonth < 10) nmonth = "0"+nmonth;
	var nday	= now.getDate();	if(nday < 10) nday = "0"+nday;
	var c = nyear+nmonth+nday;

	//b �� c ���ϱ�
	for(i=0; i<2; i++) b=b.replace('-','');
	if(b >= c) return true; 
	else return false;
}
//������ �����Ͽ� �� ���� �� �� ����ϴ� �Լ�
function DaysInMonth(WhichMonth, WhichYear)
{
  var DaysInMonth = 31;
  if (WhichMonth == "4" || WhichMonth == "6" || WhichMonth == "9" || WhichMonth == "11") DaysInMonth = 30;
  if (WhichMonth == "2" && (WhichYear/4) != Math.floor(WhichYear/4))	DaysInMonth = 28;
  if (WhichMonth == "2" && (WhichYear/4) == Math.floor(WhichYear/4))	DaysInMonth = 29;
  return DaysInMonth;
}
-->
</script>
</head>

<BODY topmargin="0" leftmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../mr/images/blet.gif"> A/S�������</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300><a href="javascript:saveResult();"><img src="../mr/images/bt_save.gif" border="0" align="absmiddle"></a> <a href="javascript:cancelResult();"><img src="../mr/images/bt_cancel.gif" border="0" align="absmiddle"></a></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<form method=post name="sForm" style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">�����о�</td>
           <td width="37%" height="25" class="bg_04">
				<select name="as_field" style=font-size:9pt;color="black";>  
				<%
					String sel = "";
					for(int si=0; si<field_cnt; si++) {
						out.println("<option value='"+asfield[si][1]+"'>"+asfield[si][0]+"</option>");
					}
				%>
				</select></td>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">����Ͻ�</td>
           <td width="37%" height="25" class="bg_04"><%=anbdt.getDate()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
		    <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">��û��</td>
           <td width="37%" height="25" class="bg_04">
				<input type='text' name='request_name' value=''></td>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">��û��</td>
           <td width="37%" height="25" class="bg_04">
				<input type='text' name='request_date' value='<%=anbdt.getDate()%>' size=10 readonly> <A Href="Javascript:OpenCalendar('request_date');"><img src="../mr/images/bt_calendar.gif" border="0" align='absmiddle'></A></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04">
				<input type="radio" checked name="as_type" value='1'>�湮����	
				<input type="radio" name="as_type" value='2'>�������� </td>
		   <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">ó������</td>
           <td width="37%" height="25" class="bg_04">
				<input type="radio" checked name="as_result" value='1'>�Ϸ�	
				<input type="radio" name="as_result" value='2'>��ó�� </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">����Ϸù�ȣ</td>
           <td width="37%" height="25" class="bg_04">
				<input type='text' name='serial_no' value=''> [S/W�� ����]</td>
		   <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">�湮��</td>
           <td width="37%" height="25" class="bg_04">
				<input type='text' name='as_date' value='<%=anbdt.getDate()%>' size=10 readonly> <A Href="Javascript:OpenCalendar('as_date');"><img src="../mr/images/bt_calendar.gif" border="0" align='absmiddle'></A></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">A/S����</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<textarea rows="9" name="as_content" cols="93" style="background:#FFFFFF;border:1 solid #787878;"></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">�̽�����</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<textarea rows="6" name="as_issue" cols="93" style="background:#FFFFFF;border:1 solid #787878;"></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">��������</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<textarea rows="5" name="as_delay" cols="93" style="background:#FFFFFF;border:1 solid #787878;"></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
</td></tr></table>
<input type="hidden" name="mode" value="">
<input type="hidden" name="register_no" value=""> 
<input type="hidden" name="register_date" value=""> 
<input type="hidden" name="code" value="C02">			<!-- �������� -->
<input type="hidden" name="worker" value="">			<!-- �������� -->
<input type="hidden" name="company_no" value="C02">		<!-- �������� -->
<input type="hidden" name="value_request" value="0">
</form>

</body>
</html>
