<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "��ܰ��� ����"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.dms.entity.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.file.textFileReader"
%>
<%
	//-----------------------------------
	//�ʱ�ȭ ����
	//-----------------------------------
	com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
	com.anbtech.date.anbDate anbdt = new anbDate();							//����
	textFileReader text = new com.anbtech.file.textFileReader();			//�������� �б�
	
	//-----------------------------------
	// ��������
	//-----------------------------------
	String user_name="";			//����� �̸�
	String doc_id="";				//������ȣ
	String slogan="";				//���ΰ�
	String title_name="";			//�μ� Title��
	String in_date="";				//�������		
	String receive="";				//����
	String reference="";			//����
	String sending="";				//�߽�
	String subject="";				//����
	String address="";				//�߽��� �ּ�
	String tel="";					//��ȭ��ȣ
	String fax="";					//�ѽ���ȣ
	String bon_path="";				//�������� Ȯ��path
	String bon_file="";				//�������� ���ϸ�
	String read_con="";				//��������
	String firm_name="";			//�߽źμ���
	String representative="";		//�߽źμ� ��ǥ��	
	String fname="";				//����:���Ͽ�����	
	String sname="";				//����:���������		
	String ftype="";				//����:����Ȯ���ڸ�	
	String fsize="";				//����:����ũ��
	String[][] addFile;				//÷�ΰ��ó��� ���
	String module_name="";			//�����������
	String module_add="";			//�����μ� �ּ�
	String mail="";					//���ڿ�������
	String mail_add="";				//���ڿ����ּ�(���/�̸�;)
	
	//-----------------------------------
	//	�������� ���� �б�
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_One");
	table = new OfficialDocumentTable();
	Iterator table_iter = table_list.iterator();
	
	if(table_iter.hasNext()){
		table = (OfficialDocumentTable)table_iter.next();

		user_name=table.getUserName();				//����� �̸�
		doc_id=table.getDocId(); 
			if(doc_id == null) doc_id = "";			//������ȣ
		slogan=table.getSlogan();					//���ΰ�
		title_name=table.getTitleName();			//�μ� Title��
		in_date=table.getInDate();					//�������
			in_date = in_date.substring(0,10);
		receive=table.getReceive();;				//����
		reference=table.getReference();				//����
			if(reference == null) reference = "";
		sending=table.getSending();					//�߽�
		subject=table.getSubject();					//����
		address=table.getAddress();					//�߽��� �ּ�
		tel=table.getTel();							//��ȭ��ȣ
		fax=table.getFax();							//�ѽ���ȣ
		bon_path=table.getBonPath();				//�������� Ȯ��path
		bon_file=table.getBonFile();				//�������� ���ϸ�
		firm_name=table.getFirmName();				//�߽źμ���
		representative=table.getRepresentative();	//�߽źμ� ��ǥ��	
		fname=table.getFname();						//����:���Ͽ�����	
		sname=table.getSname();						//����:���������		
		ftype=table.getFtype();						//����:����Ȯ���ڸ�	
		fsize=table.getFsize();						//����:����ũ��
		module_name=table.getModuleName();			//�����������
		module_add=table.getModuleAdd();			//�����μ��ּ�
		mail=table.getMail();						//���ڿ�������
		mail_add=table.getMailAdd();				//���ڿ����ּ�(���/�̸�;)
	}

	//���������б�
	String full_path = upload_path + bon_path + "/bonmun/" + bon_file;
	read_con = text.getFileString(full_path);

	//÷�������о� �迭�� ���
	if(fname == null) fname = "";
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	addFile = new String[cnt][4];
	for(int i=0; i<cnt; i++) for(int j=0; j<4; j++) addFile[i][j]="";

	if(fname.length() != 0) {
		StringTokenizer f = new StringTokenizer(fname,"|");		//���ϸ� ���
		int m = 0;
		while(f.hasMoreTokens()) {
			addFile[m][0] = f.nextToken();
			addFile[m][0] = addFile[m][0].trim(); 
			if(addFile[m][0] == null) addFile[m][0] = "";
			m++;
		}
		StringTokenizer t = new StringTokenizer(ftype,"|");		//����type ���
		m = 0;
		while(t.hasMoreTokens()) {
			addFile[m][1] = t.nextToken();
			addFile[m][1] = addFile[m][1].trim();
			if(addFile[m][1] == null) addFile[m][1] = "";
			m++;
		}
		StringTokenizer s = new StringTokenizer(fsize,"|");		//����ũ�� ���
		m = 0;
		while(s.hasMoreTokens()) {
			addFile[m][2] = s.nextToken();
			addFile[m][2] = addFile[m][2].trim();
			if(addFile[m][2] == null) addFile[m][2] = "";
			m++;
		}
		StringTokenizer o = new StringTokenizer(sname,"|");		//�������� ���
		m = 0;
		int no = 1;
		while(o.hasMoreTokens()) {
			addFile[m][3] = o.nextToken();
			addFile[m][3] = addFile[m][3].trim() + ".bin";			
			if(addFile[m][3] == null) addFile[m][3] = "";
			m++;
			no++;
		}
	}	
%>

<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title>�系����</title>
<LINK href="../ods/css/style.css" rel=stylesheet>
</head>


<BODY leftmargin='0' topmargin='10' marginwidth='0' marginheight='0'>
<center>

<% //slogan, title_name ��� 
	int div_h = 75;
	out.println("<font size=2>&nbsp;"+slogan+"&nbsp;</font><br>");
	if(title_name.length() > 0) {
		out.println("<font size=6>&nbsp;"+title_name+"&nbsp;</font><br>");
	} else {
		out.println("<font size=6>&nbsp;&nbsp;</font><br>");
		div_h = 72;
	}
%>

<table width='640' border="0" cellspacing="0" cellpadding="0">
	<tr><td width=50% align='left' height=20><img src='../ods/images/logo.jpg' align='absmiddle' border='0'></td>
		<td width=50% align='right'><div id="print" style="position:relative;visibility:visible;">
			<a href='Javascript:winprint();'><img src='../ods/images/bt_print.gif' align='absmiddle' border='0'></a> <!-- ��� -->
			<a href='Javascript:self.close();'><img src='../ods/images/bt_close.gif' align='absmiddle' border='0'></a> <!-- �ݱ� -->
		</div></td>
	</tr>
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0'>
	<tr><td width=100% height="2"></td></tr>
	<TR><TD width=100% height='2' bgcolor='#9CA9BA'></TD></TR>
	<tr><td width=100% height="5"></td></tr>
</table>
	
<table width='640' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#9CA9BA"> 
	<tr>
		<td width="90" height="20" align="center" valign="middle">������ȣ :</td>
		<td width="230" height="20" align="left" valign="middle"><%=doc_id%></td>
		<td width="90" height="20" align="center" valign="middle">
			��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�� :</td>
		<td width="230" height="20" align="left" valign="middle"><%=in_date%></td>
	</tr>
	<tr>
		<td width="90" height="20" align="center" valign="middle">
			��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�� :</td>
		<td width="230" height="20" align="left" valign="middle"><%=receive%></td>
		<td width="90" height="20" align="center" valign="middle">�� �� �� : </td>
		<td width="230" height="20" align="left" valign="middle"><%=user_name%></td>
	</tr>
	<tr>
		<td width="90" height="20" align="center" valign="middle">
			��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�� :</td>
		<td width="550" height="20" align="left" valign="middle" colspan=3><%=reference%></td>
	</tr>
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
	<tr>
		<td width="100%" height="10" align="left" valign="middle" colspan=2></td>
	</tr>
	<tr>
		<td width="90" height="30" align="center" valign="middle">
			<font size=3><b>��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�� :</b></font></td>
		<td width="550" height="30" align="left" valign="middle">
			<font size=3><b><%=subject%></b></font></td>
	</tr>
	<tr><td width=100% height="2" bgcolor='#9CA9BA' colspan=2></td></tr>
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
	<tr>
		<td width="640" height="10" align="left" valign="top"></td>
	</tr>
	<tr>
		<td width="640" height="360" align="left" valign="top">
		<textarea rows=40 cols=88 style="border:1px solid #787878;" readonly><%=read_con%></textarea></td>
	</tr>
	<tr>
		<td width="100%" height="10" align="left" valign="middle" colspan=2></td>
	</tr>
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
	<tr>
		<td width="90" height="60" align="center" valign="middle">÷������ : </td>
		<td width="550" height="60" align="left" valign="middle">
		<%
		for(int i=0; i<cnt; i++) {
			out.println("&nbsp;<a href='../ods/attach_download.jsp?fname="+addFile[i][0]+"&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]+"&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a><br>");
		} 
		%>
		</td>
	</tr>
</table>

<font size=6><b><%=firm_name%></b></font><br>
<font size=6><b><%=representative%></b></font>

</center>
</body>
</html>

<script language=javascript>
<!--
//����ϱ�
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
-->
</script>
