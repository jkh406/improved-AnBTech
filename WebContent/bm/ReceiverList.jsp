<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage="../admin/errorpage.jsp" 
%>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"%>

<%
	String target_list = Hanguel.toHanguel(request.getParameter("target"));
	int sep_no = target_list.indexOf("|");

	String target = "",user_list = "";
	if(sep_no != -1) {
		target = target_list.substring(0,sep_no);
		user_list = target_list.substring(sep_no+1,target_list.length());
	} else target = target_list;
%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<TITLE>�� ������ 5</TITLE>
</HEAD>

<BODY topmargin="5" leftmargin="0">
<FORM name="listForm" action="">
<!-- ��ܿ��� -->
<TABLE><TR><TD height='5'></TD></TR></TABLE>
<!-- �׵θ� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="120" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD>

	<!--����-->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="225" align='center'>
		<TR height='25px;'>
			<TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;" BACKGROUND='../bm/images/title_bm_bg.gif'>
			<font color="#4D91DC"><b>BOM������ ����Ʈ</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD height='4'></TD></TR>
		<TR><TD width="100%" height="130" valign="top" align='middle'>
			<!-- ���� ����Ʈ ����-->
			<SELECT size="8" name="user_list" multiple>
				<OPTGROUP label='-----------------'>
			 <%
				if(user_list.length() > 0) {
					StringTokenizer list = new StringTokenizer(user_list,";");
					while(list.hasMoreTokens()) {
						String user = list.nextToken()+";\r";
						if(user.length() > 5) out.println("<option value='"+user+"'> "+user+" </option>");
					}
				}
			  %>
				</SELECT>
			</TD></TR>
		<TR><TD width="100%" height="25" valign='top' style='padding-left:20px;'>
				<a href='javascript:delSelected();'><img src='../bm/images/bt_del_sel.gif' border='0'></a></td></TR>
	</TABLE>

</TD></TR></TABLE><!--�׵θ� ��-->

<!-- �߰����� -->
<TABLE><TR><TD height='7px;'></TD></TR></TABLE>

<!-- �׵θ� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="70" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD colspan='5'>	

	<!-- ���� -->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="225" align='left'>
		<TR height='25px;'><TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='../bm/images/title_bm_bg.gif' colspan='5'><font color="#4D91DC"><b>BOM������ ���ù��</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD width="100%" height="100" valign="middle" bgcolor="F5F5F5" style='padding-left:5px;padding-right:5px;'>
			<font color="565656" style="font-size:9pt;">
			1.BOM���� ����� �˻�ȭ�鿡�� <br>&nbsp;&nbsp;&nbsp;�˻��Ѵ�.<br>
			2.�˻��� ����� �̸��� Ŭ���Ͽ� <br>&nbsp;&nbsp;&nbsp;����Ʈ�� �߰��Ѵ�.<br>
			3.���ÿϷ� ��ư�� ���� �����Ѵ�.
			</font>
			</TD></TR></TABLE>

</TD></TR></TABLE><!--�׵θ�-->

</FORM>
</BODY>
</HTML>

<SCRIPT language="javascript">
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
	var cnt = 0;
	for(i=0;i<from.length;i++)
	{
		user_list += from.options[i].value + "\n";
		cnt++;
	} //for
	if(cnt > 1) { alert('�����ڴ� 1�� �Է��մϴ�.'); return; }

	parent.opener.document.<%=target%>.value = user_list;
	top.close();
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
	user = id + "/" + name + ";";

	if(type == "div"){
		alert("�μ����� ������ �� �� �����ϴ�.");
		return;
	}

	var where_list = document.listForm.user_list;

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
</SCRIPT>