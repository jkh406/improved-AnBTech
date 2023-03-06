<%@ include file="../../../admin/configHead.jsp"%>
<%@ page		
	info= "�������������꼭 �ۼ�"		
	contentType = "text/html; charset=euc-kr"
	errorPage = "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.file.FileWriteString"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	FileWriteString text = new com.anbtech.file.FileWriteString();			//������ ���Ϸ� ���
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//������� (��,��)
	normalFormat money = new com.anbtech.util.normalFormat("#,000");		//������� (���)
	StringProcess str = new com.anbtech.text.StringProcess();				//���ڿ� �ٷ��

	String filepath =  upload_path+"/es/"+login_id+"/addfile";
	text.setFilepath(filepath);		//directory�����ϱ�

	int maxFileSize = 10;
	com.oreilly.servlet.MultipartRequest multi;
	multi = new com.oreilly.servlet.MultipartRequest(request,filepath,maxFileSize*1024*1024,"euc-kr");

	String query = "";
	String writer_id = "";			//�����(�븮����ϼ��� ����) ���
	String writer_name = "";		//�����(�븮����ϼ��� ����) �̸�

	String user_name = "";			//�ش��� ��
	String rank_code = "";			//�ش��� ����code
	String user_rank = "";			//�ش��� ����
	String div_id = "";				//�ش��� �μ��� �����ڵ�
	String div_name = "";			//�ش��� �μ���
	String div_code = "";			//�ش��� �μ��ڵ�
	
	int period_n = 0;				//from ~ to �Ⱓ : ��
	int period = 0;					//from ~ to �Ⱓ : ��
	/*********************************************************************
	 	�����(login) �˾ƺ���
	*********************************************************************/	
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(Column);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//����� ���
		writer_name = bean.getData("name");				//����� ��
	} //while


	/*********************************************************************
	 	�ش��� ���� �˾ƺ��� (�����)
	*********************************************************************/	
	String user_id = multi.getParameter("user_id"); if(user_id == null) user_id = login_id;
	String[] idColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+user_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_name = bean.getData("name");				//�ش��� ��
		rank_code = bean.getData("ar_code");			//�ش��� ���� code
		user_rank = bean.getData("ar_name");			//�ش��� ����
		div_id = bean.getData("ac_id");					//�ش��� �μ��� �����ڵ�
		div_name = bean.getData("ac_name");				//�ش��� �μ��� 
		div_code = bean.getData("ac_code");				//�ش��� �μ��ڵ�
	} //while

	/*********************************************************************
	 	����� ������ �˾ƺ���
	*********************************************************************/	
	String receiver_id = multi.getParameter("receiver_id");	if(receiver_id == null) receiver_id = "";	
	if(receiver_id.length() == 0) receiver_id = writer_id;
	String[] recColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(recColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+receiver_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	String receiver_name = "";
	while(bean.isAll()) {
		receiver_name = bean.getData("name");			//������ ��
	} //while

	String doc_chuljang = "BT_001";
	/*********************************************************************
	 	����� �׸� ����
	*********************************************************************/	
	String[] csColumn = {"code","code_name"};
	bean.setTable("system_minor_code");			
	bean.setColumns(csColumn);
	bean.setClear();
	bean.setOrder("code DESC");	
	query = "WHERE type = 'BT_COST'";
	bean.setSearchWrite(query);
	bean.init_write();

	int cnt = bean.getTotalCount();
	String[][] btrip = new String[cnt][2];

	int ac_cnt = 0;
	while(bean.isAll()) {
		btrip[ac_cnt][0] = bean.getData("code");			//��������ڵ�
		btrip[ac_cnt][1] = bean.getData("code_name");		//���������
		ac_cnt++;
	} //while

	/*********************************************************************
	 	���� ������ ó��
	*********************************************************************/	
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";	//���缱
	String change_factor = multi.getParameter("change_factor"); 
		if(change_factor == null) change_factor = "";								//�������
	String change_note = multi.getParameter("change_note"); 
		if(change_note == null) change_note = "";									//���泻��
	String rec = multi.getParameter("doc_receiver"); if(rec == null) rec = "";		//�μ��ΰ���
	String tel = multi.getParameter("doc_tel"); if(tel == null) tel = "";			//��޿���ó
	
	
	//���ݾװ� ���⳻���� �迭�� ���
	int costcnt = btrip.length - 1;
	String[][] cost = new String[costcnt][5];
	String c_code = "";		//�����ڵ�
	String c_cost = "";		//�����û ���
	String c_cont = "";		//�����û ���⳻��
	int sum = 0;			//�����û ��� �հ�

	String a_cost = "";		//�������� ���
	String a_cont = "";		//�������� ���⳻��
	int a_sum = 0;			//�������� ��� �հ�

	int d_sum = 0;			//������������ ��� �հ�

	for(int c=0,m=1; c < costcnt; c++,m++) {
		//�����û�ݾ�
		c_code = "code"+m;
		c_cost = "cost"+m;
		c_cont = "cont"+m;
		cost[c][0] = multi.getParameter(c_code);	if(cost[c][0] == null) cost[c][0] = "";
		cost[c][1] = multi.getParameter(c_cost);	if(cost[c][1] == null) cost[c][1] = "0";
		cost[c][2] = multi.getParameter(c_cont);	if(cost[c][2] == null) cost[c][2] = "";
		
		//����հ� ����ϱ�
		cost[c][1] = str.repWord(cost[c][1],",","");	//�޸� ����
		try { sum += Integer.parseInt(cost[c][1]); } catch (Exception e) {cost[c][1] = "0";}
		
		//��������ݾ�
		a_cost = "a_cost"+m;
		a_cont = "a_cont"+m;
		cost[c][3] = multi.getParameter(a_cost);	if(cost[c][3] == null) cost[c][3] = "0";
		cost[c][4] = multi.getParameter(a_cont);	if(cost[c][4] == null) cost[c][4] = "";
		
		//�����հ� ����ϱ�
		cost[c][3] = str.repWord(cost[c][3],",","");	//�޸� ����
		try { a_sum += Integer.parseInt(cost[c][3]); } catch (Exception e) {cost[c][3] = "0";}
	}
	d_sum = a_sum - sum;		//���� ����

	//out.print("sum : " + fmt.toDigits(sum));

%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
<style type="text/css">
<!--
.money {
	BORDER-RIGHT: #a4a4a4 1px solid; BORDER-TOP: #a4a4a4 1px solid; BORDER-LEFT: #a4a4a4 1px solid; BORDER-BOTTOM: #a4a4a4 1px solid; TEXT-ALIGN: right
}
-->
</style>
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return true">
<form action="chuljanb_jeondo_FP_write.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> �������������꼭 �ۼ�</TD>
			  </TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../../images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../../images/bt_sangsin.gif" border="0" align="absmiddle"></a> <!-- ��� -->
				<a href="Javascript:history.go(-1);"><img src="../../images/bt_cancel.gif" border="0" align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<br>��<br>��</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=line%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=36% align=left><!-- ����ĭ-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">�����</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- ����ĭ �� -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
	     <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04"> <input type="radio" name="change_yn" value='��'>�� <input type="radio" name="change_yn" value='��' checked>��</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�ۼ�����</td>
           <td width="37%" height="25" class="bg_04"><%=anbdt.getDate()%></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"> <input size="40" type="text" name="change_factor" value='<%=change_factor%>'></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">���泻��</td>
           <td width="37%" height="25" class="bg_04"> <input size="40" type="text" name="change_note" value='<%=change_note%>'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr>
			<td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�� �� ��<br>��������</td>
			<td width="87%" height="25" class="bg_04" colspan='3'>
				<TABLE cellSpacing=2 cellPadding=0 width=100% border=0 bordercolordark=white bordercolorlight=#9CA9BA>
					<tr><td height=23 class=bg_05 width='20%' align='center'>����</td>
						<td height=23 class=bg_05 width='20%' align='center'>����</td>
						<td height=23 class=bg_05 width='20%' align='center'>������</td>
						<td height=23 class=bg_05 width='40%' align='center'>��������</td></tr>
					<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
					<tr><td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="sdate1" value='' readonly><a href="Javascript:OpenCalendar('sdate1');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="edate1" value='' readonly><a href="Javascript:OpenCalendar('edate1');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="15" type="text" name="dest1" value=''></td>
						<td height=23 class=bg_04 width='40%' align='left'>
							<input size="35" type="text" name="note1" value=''></td></tr>
					<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
					<tr><td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="sdate2" value='' readonly><a href="Javascript:OpenCalendar('sdate2');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="edate2" value='' readonly><a href="Javascript:OpenCalendar('edate2');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="15" type="text" name="dest2" value=''></td>
						<td height=23 class=bg_04 width='40%' align='left'>
							<input size="35" type="text" name="note2" value=''></td></tr>
					<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
					<tr><td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="sdate3" value='' readonly><a href="Javascript:OpenCalendar('sdate3');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="edate3" value='' readonly><a href="Javascript:OpenCalendar('edate3');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="15" type="text" name="dest3" value=''></td>
						<td height=23 class=bg_04 width='40%' align='left'>
							<input size="35" type="text" name="note3" value=''></td></tr>
					<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
					<tr><td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="sdate4" value='' readonly><a href="Javascript:OpenCalendar('sdate4');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="10" type="text" name="edate4" value='' readonly><a href="Javascript:OpenCalendar('edate4');"><img src="../../images/bt_calendar.gif" border="0" align='absbottom'></a></td>
						<td height=23 class=bg_04 width='20%' align='center'>
							<input size="15" type="text" name="dest4" value=''></td>
						<td height=23 class=bg_04 width='40%' align='left'>
							<input size="35" type="text" name="note4" value=''></td></tr>
					<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
				</TABLE>
			</td>
		 </tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�����û����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input size=45 type="text" name="ref_name" class="text_01" readonly>
		   <a href="Javascript:searchRefDocument();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	</tbody></table>
<%	/************************************************************
	 ����� û���ݾ׹� ����ݾ� ���
	 ************************************************************/
%>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�� �� ��<br>�� ��</td>
           <td width="87%" class="bg_04" colspan="3" valign='top'>
			<%
				out.print("<TABLE cellSpacing=2 cellPadding=0 width=100% border=0 bordercolordark=white bordercolorlight=#9CA9BA>");
				out.print("<tr><td height=23 class=bg_05 width='20%' align='center'>�׸�</td>"); 
				out.print("<td class=bg_05 width='25%' align='center'>�� ��</td>"); 
				out.print("<td class=bg_05 width='55%' align='center'>���⳻��</td></tr>");

				for(int n=1,p=0; p < cnt; n++,p++) {
					out.print("<tr><td class=bg_07 align='center'><input type='hidden' name='code"+n+"' value='"+btrip[p][0]+"'>");
					out.print(btrip[p][1]+"</td>");
					out.print("<td class=bg_07><input class='money' size=15 type='text' name='a_cost"+n+"' value='0' style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this);'></td>");//������(�ݾ�)
					out.print("<td class=bg_07><input size=40 type='text' name='cont"+n+"' value=''></td></tr>");
				}
				out.print("<tr><td class=bg_07 align='center'><b>�� ��</b></td>");
				out.print("<td class=bg_07><input class='money' size=15 type='text' name='a_sum' value='0' readonly></td>");
				out.print("<td class=bg_07>&nbsp;<b>�� ��</b> <input class='money' size=15 type='text' name='sum' value='0' readonly>&nbsp;");
				out.print("<b>�� ��</b> <input class='money' size=15 type='text' name='d_sum' value='0' readonly></td></tr>");
				out.print("</table>");
			%>
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="87%" height="25" class="bg_04" colspan='3'><input size=10 type="text" name="receiver_name" value='<%=receiver_name%>'> <a href="Javascript:searchReceiver();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td>  
		 </tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>  
</td></tr></table>

<input type="hidden" name="user_id" value='<%=user_id%>'>
<input type="hidden" name="user_code" value='<%=rank_code%>'>
<input type="hidden" name="user_rank" value='<%=user_rank%>'>
<input type="hidden" name="div_id" value='<%=div_id%>'>
<input type="hidden" name="div_code" value='<%=div_code%>'>
<input type="hidden" name="div_name" value='<%=div_name%>'>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value=''>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='ac_cnt' value='<%=ac_cnt%>'>
<input type='hidden' name='doc_chuljang' value='<%=doc_chuljang%>'>
<input type="hidden" name="receiver_id" value='<%=receiver_id%>'>
<input type="hidden" name="receiver_date" value='<%=anbdt.getDate()%>'>
<input type="hidden" name="ref_id" value=''>
<input type="hidden" name="plid" value=''>
</form>
</body>
</html>


<script language=javascript>
<!--
//�����ڵ� ��Ͽ��� Ȯ���ϱ�
var code = '<%=doc_chuljang%>';
if(code.length == 0) {
	alert("�����ڵ尡 ��ϵǾ����� �ʽ��ϴ�. �����ڿ��� ������ ����Ͻʽÿ�.");
}

//�����û���� ã��
function searchRefDocument()
{
	wopen("searchChuljang.jsp?target_id=eForm.ref_id&target_name=eForm.ref_name","ref_id","520","310","scrollbars=no,toolbar=no,status=no,resizable=no");

}
//������ ã��
function searchReceiver()
{
	wopen("searchUser.jsp?target=eForm.receiver_id/eForm.receiver_name","proxy","250","380","scrollbars=no,toolbar=no,status=no,resizable=no");

}
//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../Calendar.jsp?FieldName=" + FieldName;
	newWIndow = wopen(strUrl, "Calendar", "180", "250","scrollbars=no,toolbar=no,status=no,resizable=no");
}

//����ϱ�
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}

//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

//�ݾ� õ������ �޸��ֱ�
function InputMoney(input){
	str = input.value;
	str = unComma(str);
	str = Comma(str);
	input.value = str;

	sumMoney();
}
//�Ѿ�ǥ���ϱ�
function sumMoney(){
	var n = '<%=cnt%>';
	var sum = document.eForm.sum.value;
	sum = unComma(sum);
	var a_sum = 0;
	var d_sum = 0;
	for(i=0,j=1; i<n; i++,j++) {
		a_sum += unCommaObj(eval("document.eForm.a_cost"+j+".value"));
	}
	document.eForm.a_sum.value=Comma(a_sum);
	if(a_sum >= sum) document.eForm.d_sum.value=Comma(a_sum-sum);
	else document.eForm.d_sum.value="-"+Comma(a_sum-sum);
}
function isNumObj(obj)
{
	for (var i = 0; i < obj.value.length ; i++){
		chr = obj.value.substr(i,1);		
		chr = escape(chr);
		key_eg = chr.charAt(1);
		if (key_eg == 'u'){
			key_num = chr.substr(i,(chr.length-1));			
			if((key_num < "AC00") || (key_num > "D7A3")) { 
				event.returnValue = false;
			} 			
		}
	}
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		
	} else {
		event.returnValue = false;
	}
}
function Comma(num) {
	re = /(\d+)/;
	if(re.test(num)){ 
		re.exec(num); num = RegExp.$1; 
		re = /(\d+)(\d{3})/;
		while(re.test(num)){ num = num.replace(re,"$1,$2"); }
	}
    return (num);
}
function unComma(str) {
	return str.replace(/,/g,"");
}

//obj�ι޾� �޸� ���ֱ�
function unCommaObj(input) {
   var inputString = new String;
   var outputString = new String;
   var outputNumber = new Number;
   var counter = 0;
   if (input == '')
   {
	return 0
   }
   inputString=input;
   outputString='';
   for (counter=0;counter <inputString.length; counter++)
   {
      outputString += (inputString.charAt(counter) != ',' ?inputString.charAt(counter) : '');
   }
   outputNumber = parseFloat(outputString);
   return (outputNumber);
}
//���缱 �������� 
function eleApprovalManagerLineSelect()
{
	var target="eForm.doc_app_line&anypass=Y" 
	wopen("../eleApproval_Share.jsp?target="+target,"eleA_app_search_select","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");
}
//���� ��� 
function eleApprovalRequest()
{
	if (eForm.doc_app_line.value =="") { alert("���缱�� �Է��Ͻʽÿ�."); return; }
	if (eForm.ref_name.value =="") { alert("�����û������ �Է��Ͻʽÿ�."); return; }
	
	 //���缱 �˻�
	data = eForm.doc_app_line.value;		//���缱 ����
	s = 0;								//substring������
	e = data.length;					//���ڿ� ����
	decision = agree = 0;
	for(j=0; j<e; j++){
		ocnt = data.indexOf("\n");
		j += ocnt + 1;

		rstr = data.substring(s,ocnt);
		if(rstr.indexOf("����") != -1) decision++;
		if(rstr.indexOf("����") != -1) agree++;
	
		if(rstr.length == 0) j = e;
		data = data.substring(ocnt+1,e);
	}
	if(decision == 0) { alert("�����ڰ� �������ϴ�"); return; }

	//�����ϱ�
	var ref_name = document.eForm.ref_name.value;
	var doc_sub = "�������������꼭 : ["+ref_name+"]";

	document.onmousedown=dbclick;

	//�ϰ����� ����������
	document.eForm.action='../../../servlet/GeunTaeServlet';
	document.eForm.mode.value='CHULJANG_JEONDO';	
	document.eForm.app_mode.value='REQ';
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();

}

function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</script>
