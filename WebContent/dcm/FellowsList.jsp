<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"%>

<%
	//String target = request.getParameter("target");
	String tg_list="",target="",users="";
	tg_list = Hanguel.toHanguel(request.getParameter("target"));
	
	int bar_no = tg_list.indexOf("|");
	int len_no = tg_list.length();
	if(bar_no > 1) {
		target = tg_list.substring(0,tg_list.indexOf("|"));
		users = tg_list.substring(tg_list.indexOf("|")+1,tg_list.length());
	}
%>

<HTML>
<HEAD>
<META content="text/html;charset=euc-kr" http-equiv=content-type>
<LINK href="./css/style.css" rel=stylesheet>

</HEAD>
<BODY topmargin="0" leftmargin=0 marginwidth=0>

<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="100%" align='left'>
  <tr>
    <td width="100%" height="30"><font color="#4D91DC"><b>���õ� ��� ����Ʈ</b></font></td></tr>
  <tr>
    <td width="100%" height="130" valign="top"><!-- ���� ����Ʈ ����-->
		<form name="listForm" method="post" style="margin:0">
		<select name="user_list" multiple size="8">
			<OPTGROUP label='----------------------'>
		<%
			StringTokenizer list = new StringTokenizer(users,";");
			while(list.hasMoreTokens()) {
				String user = list.nextToken();
				out.println("<option value='"+user+";'>"+user+";</option>");
			}

		%>
		</select></form></td></tr>
  <tr>
	<td width="100%" height="30" align='center'><a href='javascript:delSelected();'><img src='./images/bt_del_sel.gif' border='0'></a></td></tr>
  <tr>
	<td width="100%" height="30" align='center'></td></tr>
  <tr>
	<td width="100%" height="20"><font color="#4D91DC"><b>���������� ���ù��</b></font></td></tr>
  <tr><td width="100%" valign="top" bgcolor="F5F5F5"><font color="565656">
			1.�����ϰ��� �ϴ� ����� �˻�ȭ�鿡��<br>&nbsp;&nbsp;&nbsp;�˻��Ѵ�.<br>
			2.�˻��� ����� �̸��� Ŭ���Ͽ� ����Ʈ<br>&nbsp;&nbsp;&nbsp;�� �߰��Ѵ�.<br>
			3.���ÿϷ� ��ư�� ���� �����Ѵ�.</font>
</td></tr></table>

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
		user_list += from.options[i].value + "\n";
	} //for


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
	user = id+"/"+name+";";				//����� �̸��� : �ߺ��˻��

	//�μ����ý� ����
	if(type == "div"){	return; }

	//�ߺ� �߰��� �� ���� ó��
	var where_list = document.listForm.user_list;	
	var length = where_list.length; 
	for(j=0;j<length;j++) {	
		if(where_list.options[j].value == user) {
			alert('[�ߺ�]�̹� ���õ� ����Դϴ�.');
			return;
		}
	}
	//����Ʈ�� �߰�
	var option0 = new Option(id+"/"+name+";",id+"/"+name+";");	//text,value
	where_list.options[length] = option0;

}

//-->
</script>