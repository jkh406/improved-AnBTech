<%@ include file="../admin/configPopUp.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" 
	errorPage	= "../admin/errorpage.jsp"
%>
<%@ page import="java.sql.*, java.io.*, java.util.*, com.anbtech.text.Hanguel"%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	//�ʱ�ȭ
	//�۾����ȣ,�۾����,�����ȣ,���ޱ���
	String[] data = new String[4];
	for(int i=0; i<4; i++) data[i]="";

	//��ü������ 
	data[0] = request.getParameter("work_no");		if(data[0] == null) data[0]="";
	data[1] = request.getParameter("work_name");	if(data[1] == null) data[1]="";
	data[2] = request.getParameter("factory_no");	if(data[2] == null) data[2]="";
	data[3] = request.getParameter("buy_type");		if(data[3] == null) data[3]="";
	String sWord = Hanguel.toHanguel(request.getParameter("sWord"));		if(sWord == null) sWord = "";

	//�Ѱ��� �Ķ���� ó���ϱ� : from opener �� ����
	if(data[0].length() == 0) {
		String target = request.getParameter("target");
		StringTokenizer list = new StringTokenizer(target,"/");
		int t=0;
		while(list.hasMoreTokens()) {
			data[t] = list.nextToken();
			t++;
		}
	}

	//�۾����ڵ� �б�
	String sql = "",work_type="";
	if(data[3].equals("M")) work_type="1";		//�系����
	else if(data[3].equals("O")) work_type="2";	//���ְ���
	else work_type="";

	bean.openConnection();

	sql = "SELECT count(*) FROM mfg_work where factory_no='"+data[2]+"' and work_type='"+work_type+"' ";
	sql += "and work_name like '%"+sWord+"%'";
	bean.executeQuery(sql);
	bean.next();
	int cnt = Integer.parseInt(bean.getData(1));
	String[][] work = new String[cnt][2];

	sql = "SELECT * FROM mfg_work where factory_no='"+data[2]+"' and work_type='"+work_type+"' ";
	sql += "and work_name like '%"+sWord+"%' order by work_no asc";

	bean.executeQuery(sql);
	int n=0;
	while(bean.next()) {
		work[n][0] = bean.getData("work_no");					//�۾����ڵ�
		work[n][1] = bean.getData("work_name");					//�۾����
		n++;
	}

%>

<HTML><HEAD><TITLE>�۾��� �����˻�</TITLE>
<LINK href="../mm/css/style.css" rel=stylesheet></head>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<form name='sForm' method='post' style="margin:0">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="images/pop_wplace_info_search.gif"  alt='�۾��������˻�'></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=0 width="94%" border=0>
	   <tbody>
         <tr><td height="12"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1></td></tr>
         <tr>
           <td width="50%" style="padding-left:4px" background="images/bg-011.gif">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
				  <tr><td width="100%" height="250" valign='top'>
						<select MULTIPLE size='17' name='work' onChange='javascript:goWork()' style=font-size:9pt;color='black';>
						<OPTGROUP label='----------------------'>
				<%
				for(int i=0; i<cnt; i++) {
					out.print("<option  value='");
					out.print(work[i][0]+"|"+work[i][1]+"'>");
					out.println(work[i][0]+" "+work[i][1]+"</option>");
				}
				%>
						</select>
					</td></tr>
					<tr><td width="100%" height="30" bgcolor="#EAF3FD"  align="center">
						�۾���� <input type='text' name='sWord' size='10' value='<%=sWord%>'> <a href="javascript:go_search();"><img src="images/bt_search.gif" border="0" align="absmiddle"></a>
				    </td></tr>
				</table>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height="13" colspan="2"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:self.close();"><img src="images/close.gif" width="46" height="19" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=1 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr>
</table>
<input type='hidden' name='work_no' value='<%=data[0]%>'>
<input type='hidden' name='work_name' value='<%=data[1]%>'>
<input type='hidden' name='factory_no' value='<%=data[2]%>'>
<input type='hidden' name='buy_type' value='<%=data[3]%>'>
</form>
</BODY>
</HTML>
<script language='javascript'>
<!--
var work_type='<%=work_type%>';
if(work_type.length == 0) {
	alert('���������� ���� ������ �����Ͻʽÿ�. ��,����ǰ�� ���� �Է��� �ʿ䰡 �����ϴ�.'); 
	self.close();
}
//�����ϱ�
function goWork()
{
	var work = document.sForm.work.value;
	var comp = work.split('|');

	var work_no = document.sForm.work_no.value; 
	var work_name = document.sForm.work_name.value; 
	eval("opener.document." + work_no).value = comp[0]; 
	eval("opener.document." + work_name).value = comp[1]; 
	self.close();
}
//�˻��ϱ�
function go_search()
{
	document.sForm.action='searchWork.jsp';
	document.sForm.submit();
}
//-->
</script>