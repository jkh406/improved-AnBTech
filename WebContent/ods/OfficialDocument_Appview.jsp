<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "�������� ����(����Ϸ�� ��)"		
	contentType = "text/html; charset=KSC5601" 
	errorPage = "../admin/errorpage.jsp"
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
	com.anbtech.date.anbDate anbdt = new anbDate();							//����
	textFileReader text = new com.anbtech.file.textFileReader();			//�������� �б�
	
	//���缱 ����
	String pid = "";				//������ȣ
	String line="";					//�������� ���缱
	String t_line="";				//�������� ���缱 (textarea�� ǥ��)
	String wdate = "";				//����� ��� ����
	String vdate = "";				//������ ���� ����
	String ddate = "";				//������ ���� ����
	String wid = "";				//����ڻ��
	String vid = "";				//�����ڻ��
	String did = "";				//�����ڻ��
	String wname = "";				//�����
	String vname = "";				//������
	String dname = "";				//������

	String aids = "";				//������ �����
	String anames = "";				//������ �̸���
	String aranks = "";				//������ ���޵�
	String adivs = "";				//������ �μ���
	String adates = "";				//������ ���ڵ�
	String acomms = "";				//������ �ǰߵ�
	int line_cnt = 0;				//���缱�� ��µ� ���� ����

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
	String mail="";					//���ڿ�������
	String mail_add="";				//���ڿ����ּ�(���/�̸�;)

	/**********************************************************/
	//	���缱���� ���� �б�
	/**********************************************************/
	com.anbtech.dms.entity.OfficialDocumentAppTable app;					//helper 
	ArrayList app_list = new ArrayList();
	app_list = (ArrayList)request.getAttribute("App_One");
	
	app = new OfficialDocumentAppTable();
	Iterator app_iter = app_list.iterator();
	if(app_iter.hasNext()){
		app = (OfficialDocumentAppTable)app_iter.next();
		pid = app.getId();								//���缱 ���������ȣ[���ڰ��� ������ȣ�� ����]

		wdate = app.getGianDate();		wdate=wdate.trim();		//����� ��� ����
		vdate = app.getReviewDate();	vdate=vdate.trim();		//������ ���� ����
		ddate = app.getDecisionDate();	ddate=ddate.trim();		//������ ���� ����
		wid = app.getGianId();			wid=wid.trim();			//����ڻ��
		vid = app.getReviewId();		vid=vid.trim();			//�����ڻ��
		did = app.getDecisionId();		did=did.trim();			//�����ڻ��
		wname = app.getGianName();		wname=wname.trim();		//�����
		vname = app.getReviewName();	vname=vname.trim();		//������
		dname = app.getDecisionName();	dname=dname.trim();		//������
		aids = app.getAgreeIds();		aids=aids.trim();		//������ �����
		anames = app.getAgreeNames();	anames=anames.trim();	//������ �̸���
		aranks = app.getAgreeRanks();	aranks=aranks.trim();	//������ ���޵�
		adivs = app.getAgreeDivs();		adivs=adivs.trim();		//������ �μ���
		adates = app.getAgreeDates();	adates=adates.trim();	//������ ���ڵ�
		acomms = app.getAgreeComments();acomms=acomms.trim();	//������ �ǰߵ�

		line = "��� "+wname+"("+app.getGianRank()+")"+app.getGianDiv()+" "+wdate+"<br>";
		t_line = "��� "+wname+"("+app.getGianRank()+")"+app.getGianDiv()+" "+wdate+"\r";
		line_cnt++;
		if(vdate.length() > 10) {
			line += "���� "+vname+"("+app.getReviewRank()+")"+app.getReviewDiv()+" "+vdate+" "+app.getReviewComment()+"<br>";
			t_line += "���� "+vname+"("+app.getReviewRank()+")"+app.getReviewDiv()+" "+vdate+" "+app.getReviewComment()+"\r";
			line_cnt++;
		}
		if(adates.length() > 10) {
			int cnt = 0;
			for(int i=0; i<adates.length(); i++) if(adates.charAt(i) == ';') cnt++;
			String[][] agree = new String[cnt][6];
			//�迭�� ���
			String[] agr_list = {aids,anames,aranks,adivs,adates,acomms};
			for(int i=0; i<agr_list.length; i++) {
				StringTokenizer agl = new StringTokenizer(agr_list[i],";");
				int n = 0;
				while(agl.hasMoreTokens()) {
					agree[n][i] = agl.nextToken();
					n++;
				}
			}
			//line�����
			for(int i=0; i<cnt; i++) {
				line += "���� "+agree[i][1]+"("+agree[i][2]+")"+agree[i][3]+" "+agree[i][4]+" "+agree[i][5]+"<br>";
				t_line += "���� "+agree[i][1]+"("+agree[i][2]+")"+agree[i][3]+" "+agree[i][4]+" "+agree[i][5]+"\r";
				line_cnt++;
			}

		}
		line += "���� "+dname+"("+app.getDecisionRank()+")"+app.getDecisionDiv()+" "+ddate+" "+app.getDecisionComment()+"<br>";
		t_line += "���� "+dname+"("+app.getDecisionRank()+")"+app.getDecisionDiv()+" "+ddate+" "+app.getDecisionComment()+"\r";
		line_cnt++;
	}
	
	/**********************************************************/
	//	�������� ���� �б�
	/**********************************************************/
	com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_One");
	table = new OfficialDocumentTable();
	Iterator table_iter = table_list.iterator();
	
	if(table_iter.hasNext()){
		table = (OfficialDocumentTable)table_iter.next();

		user_name=table.getUserName();				//����� �̸�
		doc_id=table.getDocId(); 
			if(doc_id == null) doc_id = "��������� �ڵ�ä��";			//������ȣ
		slogan=table.getSlogan();					//���ΰ�
		title_name=table.getTitleName();			//�μ� Title��
		in_date=table.getInDate();					//�������
			in_date = in_date.substring(0,10);
		receive=table.getReceive();;				//����
		reference=table.getReference();				//����
			if(reference == null) reference = "";
		sending=table.getSending();					//�߽�
		subject=table.getSubject();					//����	
		bon_path=table.getBonPath();				//�������� Ȯ��path
		bon_file=table.getBonFile();				//�������� ���ϸ�
		firm_name=table.getFirmName();				//�߽źμ���
		representative=table.getRepresentative();	//�߽źμ� ��ǥ��	
		fname=table.getFname();						//����:���Ͽ�����	
		sname=table.getSname();						//����:���������		
		ftype=table.getFtype();						//����:����Ȯ���ڸ�	
		fsize=table.getFsize();						//����:����ũ��
		module_name=table.getModuleName();			//�����������
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
<title>��������</title>
<LINK href="../ods/css/style.css" rel=stylesheet>
</head>

<BODY leftmargin='0' topmargin='10' marginwidth='0' marginheight='0'>
<center>

<% //slogan, title_name ��� 
	int div_h = 77;
	out.println("<font size=2>&nbsp;"+slogan+"&nbsp;</font><br>");
	if(title_name.length() > 0) {
		out.println("<font size=6>&nbsp;"+title_name+"&nbsp;</font><br>");
	} else {
		out.println("<font size=6>&nbsp;&nbsp;</font><br>");
		div_h = 72;
	}

%>

<table width='640' border="0" cellspacing="0" cellpadding="0">
	<tr>
	<td width=50% align='left' height=20><img src='../ods/images/logo.jpg' align='middle' border='0' align='absmiddle'></td>
	<td width=50% align='right' height=20>
		<div id="print" style="position:relative;visibility:visible;">
			<a href='Javascript:winprint();'><img src='../ods/images/bt_print.gif' align='absmiddle' border='0'></a> <!-- ��� -->
			<a href='Javascript:self.close();'><img src='../ods/images/bt_close.gif' align='absmiddle' border='0'></a> <!-- �ݱ� -->
		</div>
	</td></tr>
    <TR><TD height='2' bgcolor='#9CA9BA' colspan="2"></TD></TR>
    <TR><TD height='5' colspan="2"></TD></TR>
</table>

<TABLE cellSpacing=0 cellPadding=0 width="640" border=1 bordercolordark="white" bordercolorlight="#9CA9BA"> 
	<tr>   
		<td width="20" height="96" rowspan=3 align="center" class="bg_05">��<p>��</td>
		<td width="420" height="96" rowspan=3 align="left" valign="top">
		<% if(line_cnt < 7) {    
			out.println(line);
		 } else {				 
			out.println("<TEXTAREA rows=7 cols=66 readOnly style='text-align:left;font-size:9pt;border:1px solid #787878;'>"+t_line+"</TEXTAREA>");
		 }	%> </td>
		<td width="20" height="96" rowspan=3 align="center" class="bg_05">��<p>��</td>                     
		<td width="60" align="center" height="22" class="bg_07">�� ��</td>            
		<td width="60" align="center" height="22" class="bg_07">�� ��</td>            
		<td width="60" align="center" height="22" class="bg_07">�� ��</td>            
	</tr>     
	<tr height="26">         
		<td width="60" height="50" align="center"><img src="../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></td>   
		<td width="60" height="50" align="center">
		<% //������ ������ ���� ǥ���ϱ� (��, �ݷ������� �ƴѰ�츸)
			if(vdate.length() == 0)	{//������
				if(ddate.length() == 0) out.println("&nbsp;");
				else out.println("����");
			} else {
				out.println("<img src='../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
			}
		%></td>   
		<td width="60" height="50" align="center">
		<%
			if(ddate.length() == 0)	{//������
				out.println("&nbsp;");
			} else {
				out.println("<img src='../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
			}
		%>	
		</td>   
	</tr>   
	<tr height="26">         
		<td width="60" height="24" align="center" class="bg_07">&nbsp;<%=wname%>&nbsp;</td>   
		<td width="60" height="24" align="center" class="bg_07">&nbsp;<%=vname%>&nbsp;</td>   
		<td width="60" height="24" align="center" class="bg_07">&nbsp;<%=dname%>&nbsp;</td>   
	</tr>   
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0'>
	<tr><td width=100% height="2"></td></tr>
	<TR><TD width=100% height='2' bgcolor='#9CA9BA'></TD></TR>
	<tr><td width=100% height="5"></td></tr>
</table>


<table width='640' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111"> 
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
		<td width="640" align="left" valign="top">
		<textarea rows=30 cols=88 style="border:1px solid #787878;" readonly><%=read_con%></textarea></td>
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

<table width='640' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
	<tr><td width=100% height="30" align="center" valign="middle" colspan=2 ></td></tr>
</table>
</center>

<div id="Mark" style="position:absolute;left:500px;top:830px;width:300px;height:100px;visibility:visible;">
	<img src='../ods/images/stamp1.gif' align='absmiddle' border='0'>
</div>

</body>
</html>

<script language='javascript'>
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
</script>