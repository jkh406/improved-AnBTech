<%@ include file= "../../admin/configPopUp.jsp"%>
<%@ page 
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"%>

<%
	//������ι��� �뺸�� �����ϱ�
	String pid = Hanguel.toHanguel(request.getParameter("pid"));
	
%>

<HTML>
<HEAD>
<META content="text/html;charset=euc-kr" http-equiv=content-type>
<LINK href="../css/style.css" rel=stylesheet>

</HEAD>
<BODY topmargin="0" leftmargin=0 marginwidth=0 >

<!-- ��ܿ��� -->
<TABLE><TR><TD height='5'></TD></TR></TABLE>
<!-- �׵θ� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="120" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD>

	<!--����-->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="230" align='center'>
		<TR height='25px;'>
			<TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;" BACKGROUND='../images/title_bm_bg.gif'>
			<font color="#4D91DC"><b>���õ� �뺸�� ����Ʈ</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD height='4'></TD></TR>
		<TR><TD width="100%" height="130" valign="top" align='middle'>
			<!-- ���� ����Ʈ ����-->
			<form name="listForm" method="post" style="margin:0">
			<select name="receivers_list" multiple size="8">
			<OPTGROUP label='------------------'>
			</select></TD></TR>
		<TR><TD width="100%" height="25" valign='top' style='padding-left:20px;'>
				<a href='javascript:delSelected();'><img src='../images/bt_del_sel.gif' border='0'></a></td></TR>
	</TABLE>

</TD></TR></TABLE><!--�׵θ� ��-->

<!-- �߰����� -->
<TABLE><TR><TD height='7px;'></TD></TR></TABLE>

<!-- �׵θ� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="70" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD colspan='5'>	

	<!-- ���� -->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="230" align='left'>
		<TR height='25px;'><TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='../images/title_bm_bg.gif' colspan='5'><font color="#4D91DC"><b>�뺸�� ���ù��</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD width="100%" height="100" valign="middle" bgcolor="F5F5F5" style='padding-left:5px;padding-right:5px;'>
			<font color="565656">
			1.�뺸�ϰ��� �ϴ� ����� �˻�ȭ�鿡��<br>&nbsp;&nbsp;&nbsp;�˻��Ѵ�.<br>
			2.�˻��� ����� �̸��� Ŭ���Ͽ� ����Ʈ<br>&nbsp;&nbsp;&nbsp;�� �߰��Ѵ�.<br>
			3.���ÿϷ� ��ư�� ���� �����Ѵ�.
			</font>
			</TD></TR></TABLE>

</TD></TR></TABLE><!--�׵θ�-->
<input type='hidden' name='mode' value='RE_API'>
<input type='hidden' name='PID' value='<%=pid%>'>
<input type='hidden' name='receivers' value=''>
</form>
</BODY>
</HTML>
<script language="javascript">
<!--
//����Ʈ���� �׸� �����ϱ�
function delSelected()
{
	var num = document.listForm.receivers_list.selectedIndex;
	if(num < 0){
		alert("������ ����� ������ �ֽʽÿ�.");
		return;
	}

	var Frm = document.listForm.receivers_list;
	var len = Frm.length;
	for (i=len-1;i>=0 ;i--) {
        if(Frm.options[i].selected == true) Frm.options[i] = null;
    }	
}

//����Ʈ�� �ִ� ���� opener �� ������
function transferList()
{
	var from = document.listForm.receivers_list;

	var receivers_list = "";
	for(i=0;i<from.length;i++)
	{
		receivers_list += from.options[i].value + "\n";
	} //for

	document.listForm.action = "../../servlet/ApprovalProcessServlet";
	document.listForm.receivers.value=receivers_list;
	document.listForm.submit();
//	top.close();
}

function addUsers(item) // item == usr|A030003/�븮/�ڵ��� , div|34/��ſ�����
{
    var fromField=item.split("|");			//�����(usr) or �μ�(div) ����	
    var type =  fromField[0];				//usr or div
	var user = fromField[1];				//������

	var fromField2=user.split("/");			// ���/����/�̸�
	var id;
	var name;
	var rank;

	if(type == "usr"){
		id = fromField2[0];
		rank = fromField2[1];
		name = fromField2[2];
	}else{
		id = fromField2[0];
		rank = "";
		name = fromField2[1];	
	}

	if(type == "div"){
		//alert("�μ����� ������ �� �� �����ϴ�.");
		return;
	}

	var where_list = document.listForm.receivers_list;

	//�ߺ� �߰��� �� ���� ó��
	var length = where_list.length;
	for(j=0;j<length;j++) {	
		if(where_list.options[j].value == user) {
			alert('[�ߺ�]�̹� �߰��� �׸��Դϴ�.');
			return;
		}
	}
	//����Ʈ�� �߰�
	var option0 = new Option(id+"/"+name+";",id+"/"+name+";");	//text,value
	where_list.options[length] = option0;
}

//-->
</script>