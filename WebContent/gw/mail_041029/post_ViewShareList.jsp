<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,java.util.StringTokenizer"%>

<%
	String target = request.getParameter("target");
%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">
<LINK href="../css/style.css" rel=stylesheet>
<title>���ϼ����θ���Ʈ</title>
</head>

<BODY topmargin="0" leftmargin=0 marginwidth=0>

<!-- ��ܿ��� -->
<TABLE><TR><TD height='5'></TD></TR></TABLE>
<!-- �׵θ� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="120" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD>

	<!--����-->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="223" align='left'>
		<TR height='25px;'>
			<TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;" BACKGROUND='../images/title_bm_bg.gif'>
			<font color="#4D91DC"><b>���� ���õ� ��� ����Ʈ</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD height='4'></TD></TR>
		<TR><TD width="100%" height="130" valign="top" align='middle'>
			<!-- ���� ����Ʈ ����-->

		<form name="listForm" method="post" style="margin:0">
		<select size="8" name="user_list" multiple>
			<OPTGROUP label='--------------------'>
<% 				//������ ������ �ҷ����� ó��
				String Rec = Hanguel.toHanguel(request.getParameter("Rec"));
				if(Rec == null) Rec = "";
								
				StringTokenizer Receivers = new StringTokenizer(Rec,";");
				while(Receivers.hasMoreTokens()) {
					String rec = Receivers.nextToken()+";";
					out.println("<option value='"+rec+"'>" + rec + "</option>");
				}
%>		</select></form></TD></TR>
		<TR><TD width="100%" height="25" valign='top' style='padding-left:10px;'>
				<a href='javascript:delSelected();'><img src='../images/bt_del_sel.gif' border='0'></a></td></TR>
	</TABLE>
</TD></TR></TABLE><!--�׵θ� ��-->

<!-- �߰����� -->
<TABLE><TR><TD height='7px;'></TD></TR></TABLE>

<!-- �׵θ� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="70" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD colspan='5'>	

	<!-- ���� -->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="223" align='left'>
		<TR height='25px;'><TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='../images/title_bm_bg.gif' colspan='5'><font color="#4D91DC"><b>���ϼ����� ���� ���</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD width="100%" height="100" valign="middle" bgcolor="F5F5F5" style='padding-left:5px;padding-right:5px;'>
			
			<font color="565656">
				1. �����ϰ��� �ϴ� ����� �˻�ȭ��<br>&nbsp;&nbsp;&nbsp;&nbsp;���� �˻��Ѵ�.<br>
				2. �˻��� ����� �̸��� Ŭ���Ͽ�<br> &nbsp;&nbsp;&nbsp;&nbsp;���� ����Ʈ�� �߰��Ѵ�.<br>
				3. ���ÿϷ� ��ư�� ���� �����Ѵ�.</font>
			</TD></TR></TABLE>

</TD></TR></TABLE><!--�׵θ�-->

</BODY>
</HTML>

<script language="javascript">
<!--
//����Ʈ���� �׸� �����ϱ�
function delSelected()
{
	var num = document.listForm.user_list.selectedIndex;
	if(num < 0){
		alert("������ ����� ������ �ֽʽÿ�.");
		return;
	}

	var Frm = document.listForm.user_list;
	var len = Frm.length;
	for (i=len-1;i>=0 ;i--) {
        if(Frm.options[i].selected == true) Frm.options[i] = null;
    }	
}

//����Ʈ�� �ִ� ���� opener �� ������
function transferList()
{
	var from = document.listForm.user_list;

	var user_list = "";
	for(i=0;i<from.length;i++)
	{
		user_list += from.options[i].value + '\n';
	} //for


	parent.opener.document.<%=target%>.value = user_list;
	top.close();
}

function addUsers(item)
{
    var fromField=item.split("|")
    var type =  fromField[0];
	var user = fromField[1];

	var fromField2=user.split("/");
	var id = fromField2[0];
	var rank = fromField2[1];
	var name = fromField2[2];

	user = id + "/" + name;

	if(type == "div"){
		alert("�μ����� ������ �� �� �����ϴ�.");
		return;
	}

	var where_list = document.listForm.user_list;

	//�ߺ� �߰��� �� ���� ó��
	var length = where_list.length;
	for(j=0;j<length;j++) {	
		if(where_list.options[j].value == user + ";") {
			alert('[�ߺ�]�̹� �߰��� �׸��Դϴ�.');
			return;
		}
	}
	//����Ʈ�� �߰�
	var option0 = new Option(user,user+";");
	where_list.options[length] = option0;
}

//-->
</script>