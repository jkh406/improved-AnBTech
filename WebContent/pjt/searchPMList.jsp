<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"%>

<%
	String target = request.getParameter("target");
%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<title>�� ������ 5</title>
</head>

<body>

<form name="listForm" action="">
  <select size="15" name="user_list" multiple>
  <OPTGROUP label='----------'>
  </select>
  <br>
  <input type='button' value='�����׸� ����' onClick='javascript:delSelected();'>
  <br><br>
  <input type='button' value='���ÿϷ�' onClick='javascript:transferList();'> <input type='button' value='���' onClick='javascript:top.close();'>
  
	

</form>

</body>

</html>

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
	//1�� ��û����
	if(length == 1) { alert('�Ѹ� ���� �����մϴ�.'); return; }

	//����Ʈ�� �߰�
	var option0 = new Option(id+"/"+name+"",id+"/"+name+"");	//text,value
	where_list.options[length] = option0;
}

//-->
</script>