<%@ include file="../../admin/configHead.jsp"%>
<%@ page language="java" import="java.sql.*,com.anbtech.text.Hanguel" 
    contentType = "text/html; charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>

<%
	String target = request.getParameter("target");
	String anypass = request.getParameter("anypass");			//�ڽ��� ���缱�� ����[����,�������常]
	if(anypass == null) anypass = "";
%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">
<LINK href="../css/style.css" rel=stylesheet>
<title>���ڰ�����缱</title>
</head>

<BODY topmargin="0" leftmargin=7 marginwidth=0 oncontextmenu="return false">

<!-- ��ܿ��� -->
<TABLE><TR><TD height='5'></TD></TR></TABLE>
<!-- �׵θ� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="120" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD>

	<!--����-->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="225" align='center'>
		<TR height='25px;'>
			<TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;" BACKGROUND='../images/title_bm_bg.gif'>
			<font color="#4D91DC"><b>���缱 ����</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD height='4'></TD></TR>
		<TR><TD width="100%" height="130" valign="top" align='middle'>
			<!-- ���� ����Ʈ ����-->
		<form name="aForm" method="post" style="margin:0">
				<input type='radio' name='line_type' value='����' checked>����&nbsp;
				<input type='radio' name='line_type' value='����'>����&nbsp;
				<input type='radio' name='line_type' value='����'>����&nbsp;
				<input type='radio' name='line_type' value='�뺸'>�뺸<br>
			<select name="dec_app_line" multiple size="6" style="width:210px">
			
<%			//����� ���缱���� �ҷ����� ó��
				String callD = Hanguel.toHanguel(request.getParameter("DATA"));	//���缱 ������
				String callNum = Hanguel.toHanguel(request.getParameter("NO"));	//���缱 ����
				int callN = 0;
				if(callNum != null){
					callN = Integer.parseInt(callNum);
				}
				if((callD != null) && (callNum != null)){
					for(int i=0; i< callN; i++) {
						int strN = callD.indexOf(";");
						String LD = callD.substring(0,strN);						//text����
						String VD = LD.substring(LD.indexOf(" ")+1,LD.length())+";";	//value��(�����׸������� �������� �Է����Ŀ� �����(;�� �ִ�����))
						out.println("<option value='"+VD+"'>" + LD + ";</option>");
						callD = callD.substring(strN+1,callD.length());
					} 
				} 
				%>	</select></FORM></TD></TR>
		<TR><TD width="100%" height="25" valign='top' style='padding-left:10px;'>
				<a href='javascript:delShare();'><img src='../images/bt_del_sel.gif' border='0'></a></td></TR>
	</TABLE>

</TD></TR></TABLE><!--�׵θ� ��-->

<!-- �߰����� -->
<TABLE><TR><TD height='7px;'></TD></TR></TABLE>

<!-- �׵θ� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="70" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD colspan='5'>	

	<!-- ���� -->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="225" align='left'>
		<TR height='25px;'><TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='../images/title_bm_bg.gif' colspan='5'><font color="#4D91DC"><b>���缱 ���� ���</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD width="100%" height="100" valign="middle" bgcolor="F5F5F5" style='padding-left:3px;padding-right:3px;'>
			<font color="565656">
				1. ���缱�� �����Ѵ�.<br>
				2. ���ô�� �������� �˻�ȭ�鿡�� <br>&nbsp;&nbsp;&nbsp;&nbsp;�˻� �� �˻��Ѵ�.<br>
				3. ����1,2�� �ݺ��Ͽ� ���ϴ� ���缱<br> &nbsp;&nbsp;&nbsp;&nbsp;�� �����Ѵ�.<br>
				4. ���ÿϷ� ��ư�� ���� �����Ѵ�.	
			</font>
			</TD></TR></TABLE>

</TD></TR></TABLE><!--�׵θ�-->

</BODY>
</HTML>

<!-- ****************** �޽��� ���޺κ� ****************************** -->

<script Language = "Javascript">
<!--

function addUsers(item)
{
    var fromField=item.split("|")
    var type =  fromField[0];
	var user = fromField[1];
	var is_chief = fromField[2];

	var fromField2=user.split("/");

	var id;
	var name;
	var rank;

	if(type == "usr"){
		id = fromField2[0];
		rank = fromField2[1];
		name = fromField2[2];
	}else{
		id = fromField2[0];
		rank = "�μ���";
		name = fromField2[1];	
	}


	var where_list = document.aForm.dec_app_line;
	var line_obj = document.aForm.line_type;
	var line_type;

	for(var k=0; k<line_obj.length; k++){
		if(line_obj[k].checked) line_type = line_obj[k].value;
	}

	//�ڽ��� �������� ���ϰ�
	if(id == "<%=login_id%>" && is_chief == 'N'){
		alert("�ڽ��� ������ο� ������ �� �����ϴ�.");
		return;
	} else if(id == "<%=login_id%>" && is_chief == 'Y' && "<%=anypass%>".length == 0) {
		alert("�ڽ��� ������ο� ������ �� �����ϴ�.");
		return;
	}

	//�뺸�� �ƴ� ��� �μ����� ���� ���ϰ�
	if(type == "div" && line_type != "�뺸"){
		alert("�뺸�ܿ��� �μ����� ������ �� �� �����ϴ�.");
		return;
	}

	//�뺸�� ��쿡�� �뺸->�μ��뺸�� ����
	if(type == "div") line_type = "�뺸";

	//�����ڿ� �����ڴ� �ߺ� ���� ���ϰ�
	var length = where_list.length;
	for(j=0;j<length;j++) {	
		if(where_list.options[j].value == line_type +" " + id + " " + name + " " + rank) {
			alert('[ERROR!!]'+line_type+'�ڰ� �ߺ��Ǿ����ϴ�.');
			return;
		}

		if((line_type == "����" || line_type == "����") && where_list.options[j].value.indexOf(line_type) != -1)
		{
			alert(line_type + '�ڴ� �Ѹ� ���� �����մϴ�.');
			return;
		}
		
		//���缱�� �ùٷ� �����ߴ��� üũ
		if(line_type == "����" && (where_list.options[j].value.indexOf("�뺸") != -1))
		{
			alert("���缱 ������ �߸��Ǿ����ϴ�. [����]-[����]-[����]-[�뺸] ������ �����Ͻʽÿ�.");
			return;
		}

		if(line_type == "����" && (where_list.options[j].value.indexOf("�뺸") != -1 || where_list.options[j].value.indexOf("����") != -1))
		{
			alert("���缱 ������ �߸��Ǿ����ϴ�. [����]-[����]-[����]-[�뺸] ������ �����Ͻʽÿ�.");
			return;
		}

		if(line_type == "����" && (where_list.options[j].value.indexOf("�뺸") != -1 || where_list.options[j].value.indexOf("����") != -1 || where_list.options[j].value.indexOf("����") != -1))
		{
			alert("���缱 ������ �߸��Ǿ����ϴ�. [����]-[����]-[����]-[�뺸] ������ �����Ͻʽÿ�.");
			return;
		}
	}

	//����Ʈ�� �߰�
	var option0 = new Option(line_type + " " + id + " " + name + " " + rank,line_type + " " + id + " " + name + " " + rank);
	where_list.options[length] = option0;
}


//������ �����ϱ� 
function delShare()
{
	var data = "";					//select��ü����
	var num = document.aForm.dec_app_line.selectedIndex;
	if(num < 0){
		alert("������ �׸��� ������ �ֽʽÿ�.");
		return;
	}

	if(!confirm("�����Ͻðڽ��ϱ�?"))
		return;

	document.aForm.dec_app_line.options[num] = null;
}

//���缱 �Ѱ��ֱ� (Ȯ��)
function returnSelected()
{
	var from = document.aForm.dec_app_line;			//���缱 ��� �̸� �μ����� object
	var data = "";									//�Ѱ��� ������

	var strList = "";
	var help_count = 0;	// ������ ����
	for(i=0;i<from.length;i++)
	{
		strList += from.options[i].text + "\n";
		if(strList.indexOf("����") != -1) help_count++;
	} //for
	
	if(help_count > 10){
		alert("�����ڴ� 10�� �̻� ������ �� �����ϴ�."); 
		return;
	}

	//�˻�
	if(strList.length == 0) {alert("���õ� ���缱�� �����ϴ�."); return;}
	if(strList.indexOf("����") == -1) {alert("�����ڰ� �������ϴ�."); return;}

//	alert(strList);
	parent.opener.document.<%=target%>.value = strList;
	top.close();
}

//���缱 �����ϱ� 
function submitWin()
{
	var from = document.aForm.dec_app_line;			//���缱 ��� �̸� �μ����� object
	var data = "";									//�Ѱ��� ������

	//��ü���� �б�
	var strList = "";
	for(i=0;i<from.length;i++)
	{
		strList += from.options[i].text+";";
	} //for
	wopen("eleApproval_lineSave.jsp?Lsave="+strList,"LineS","280","142","scrollbar=no,toolbar=no,status=no,resizable=no");
}

//���缱 �ҷ����� 
function submitWinCall()
{
	wopen("eleApproval_line.jsp","LineC","480","230","scrollbar=no,toolbar=no,status=no,resizable=no");
}

//���޹��� ���缱 �ݿ��ϱ� (from eleApproval_line.jsp)
function lineCall(no,data)
{
	var list = data.split(";");
	for(i=0; i<no; i++) {
		document.aForm.dec_app_line.options[i] = new Option(list[i]);
	}  
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

-->
</script>
